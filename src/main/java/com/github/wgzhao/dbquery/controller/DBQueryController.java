package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.entities.QueryResult;
import com.github.wgzhao.dbquery.entities.Sign;
import com.github.wgzhao.dbquery.param.RestResponse;
import com.github.wgzhao.dbquery.param.RestResponseBuilder;
import com.github.wgzhao.dbquery.service.DBQueryService;
import com.github.wgzhao.dbquery.service.MyPair;
import com.github.wgzhao.dbquery.service.SignService;
import com.github.wgzhao.dbquery.util.HttpUtil;
import com.github.wgzhao.dbquery.util.ParamUtil;
import com.github.wgzhao.dbquery.util.SignUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

import static com.github.wgzhao.dbquery.constant.Constants.APP_ID;
import static com.github.wgzhao.dbquery.constant.Constants.MAGIC_SIGN;
import static com.github.wgzhao.dbquery.constant.Constants.SIGN;
import static com.github.wgzhao.dbquery.constant.Constants.WHITE_IP_LIST;

@RestController
@RequestMapping(value = "/api/v1")
public class DBQueryController
{

    private static final Logger logger = LoggerFactory.getLogger(DBQueryController.class);

    @Autowired
    private DBQueryService queryService;

    @Autowired
    private SignService signService;

    @Autowired
    private Environment environment;

    private Map<String, String> queryParams;
    private Map<String, String> controlParams;

    private String errorMsg;

    @GetMapping("/")
    public String index()
    {
        return "Hello, World!";
    }

    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public RestResponse executeQuery(@RequestParam() String selectId, @RequestParam Map<String, String> allParams,
            HttpServletRequest request)
    {
        MyPair<String, QueryResult> result;
        logger.info("Begin to query with selectId {} and all params {}", selectId, allParams);
        //take the all params break into 2 parts, one is the query params, the other is the control params
        this.queryParams = ParamUtil.getQueryParams(allParams);
        this.controlParams = ParamUtil.getControlParams(allParams);

        if (!checkControlParams(request)) {
            logger.error("Control params is invalid, error msg is {}", errorMsg);
            return RestResponseBuilder.fail(400, errorMsg);
        }

        // convert all query params key to lower case
        Map<String, String> lowerQueryParams = ParamUtil.lowercaseParams(queryParams);
        result = queryService.query(selectId, controlParams.get(APP_ID), lowerQueryParams);
        if (result.getSecond() == null || result.getSecond().getResult() == null) {
            logger.error("Query failed, error msg is {}", result.getFirst());
            return RestResponseBuilder.fail(400, result.getFirst());
        }
        else {
            return RestResponseBuilder.succ((long) result.getSecond().getResult().size(), result.getSecond());
        }
    }

    /**
     * check the control params is valid or not
     *
     * @return true if valid, false if not
     */
    private boolean checkControlParams(HttpServletRequest request)
    {
        String clientIP = HttpUtil.getClientIpAddr(request);
        logger.info("client come from {}", clientIP);
        if (WHITE_IP_LIST.contains(clientIP)) {
            logger.info("client ip {} is in white ip list, DO NOT check sign", clientIP);
            return true;
        }
        // check _sign has exists
        String sign = controlParams.getOrDefault(SIGN, null);
        if (sign == null || sign.isEmpty()) {
            this.errorMsg = "签名不存在";
            return false;
        }

        if (MAGIC_SIGN.equals(sign)) {
            // it's magic ,skip all validation
            final String[] activeProfiles = environment.getActiveProfiles();
            if (!(activeProfiles[0].equals("test") || activeProfiles[0].equals("dev"))) {
                this.errorMsg = "非法签名";
                return false;
            }
        }
        else {
            // check _appId is exists or not
            String appId = controlParams.getOrDefault(APP_ID, null);
            if (appId == null || appId.isEmpty()) {
                this.errorMsg = "缺少必要的参数 " + APP_ID;
                return false;
            }

            Sign signs = signService.querySign(appId);

            if (signs == null || signs.getAppKey() == null) {
                this.errorMsg = "无效的 " + APP_ID;
                return false;
            }

            if (!SignUtil.validSign(sign, queryParams, signs)) {
                this.errorMsg = "无效签名";
                return false;
            }
        }
        return true;
    }
}
