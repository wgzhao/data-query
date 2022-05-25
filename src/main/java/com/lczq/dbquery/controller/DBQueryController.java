package com.lczq.dbquery.controller;

import com.lczq.dbquery.entities.QueryResult;
import com.lczq.dbquery.entities.SignEntity;
import com.lczq.dbquery.param.RestResponse;
import com.lczq.dbquery.param.RestResponseBuilder;
import com.lczq.dbquery.service.DBQueryService;
import com.lczq.dbquery.service.MyPair;
import com.lczq.dbquery.service.SignService;
import com.lczq.dbquery.util.ParamUtil;
import com.lczq.dbquery.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.lczq.dbquery.constant.Constants.APP_ID;
import static com.lczq.dbquery.constant.Constants.MAGIC_SIGN;
import static com.lczq.dbquery.constant.Constants.SIGN;
import static com.lczq.dbquery.constant.Constants.WHITE_IP_LIST;

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
    public RestResponse executeQuery(@RequestParam() String selectId, @RequestParam Map<String, String> allParams, HttpServletRequest request)
    {
        logger.info("Begin to query with selectId {} and all params {}", selectId, allParams);
        //take the all params break into 2 parts, one is the query params, the other is the control params
        this.queryParams = ParamUtil.getQueryParams(allParams);
        this.controlParams = ParamUtil.getControlParams(allParams);

        if (! checkControlParams(request)) {
            return RestResponseBuilder.fail(400, errorMsg);
        }

        // convert all query params key to lower case
        Map<String, String> lowerQueryParams = ParamUtil.lowercaseParams(queryParams);
        MyPair<String, QueryResult> result = queryService.query(selectId, lowerQueryParams);
        if (result.getSecond() == null || result.getSecond().getResult() == null) {
            return RestResponseBuilder.fail(400, result.getFirst());
        }
        else {
            return RestResponseBuilder.succ((long) result.getSecond().getResult().size(), result.getSecond());
        }
    }

    /**
     * check the control params is valid or not
     * @return true if valid, false if not
     */
    private boolean checkControlParams(HttpServletRequest request)
    {
        String clientIP = request.getRemoteAddr();
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
            if (! ( activeProfiles[0].equals("test") || activeProfiles[0].equals("dev"))) {
               this.errorMsg =  "非法签名";
               return false;
            }
        } else {
            // check _appId is exists or not
            String appId = controlParams.getOrDefault(APP_ID, null);
            if (appId == null || appId.isEmpty()) {
                this.errorMsg = "缺少必要的参数 " + APP_ID;
                return false;
            }

            SignEntity signEntity = signService.querySign(appId);

            if (signEntity == null || signEntity.getAppKey() == null) {
                this.errorMsg = "无效的 " + APP_ID;
                return false;
            }

            if (!SignUtil.validSign(sign, queryParams, signEntity)) {
                this.errorMsg = "无效签名";
                return false;
            }
        }
        return true;
    }

    @PutMapping(value = "/gen_sign", produces = "application/json;charset=UTF-8")
    public SignEntity genSign(@RequestHeader MultiValueMap<String, String> headers)
    {
        if (headers.isEmpty() || ! headers.containsKey("secret")) {
            return null;
        }
        if (! "lczq".equals(headers.getFirst("secret"))) {
            return null;
        }
        return signService.genSign();
    }
}
