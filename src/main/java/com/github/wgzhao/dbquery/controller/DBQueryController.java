package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.dto.QueryResult;
import com.github.wgzhao.dbquery.entities.Sign;
import com.github.wgzhao.dbquery.errors.ParamException;
import com.github.wgzhao.dbquery.service.DBQueryService;
import com.github.wgzhao.dbquery.service.SignService;
import com.github.wgzhao.dbquery.util.HttpUtil;
import com.github.wgzhao.dbquery.util.ParamUtil;
import com.github.wgzhao.dbquery.util.SignUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.github.wgzhao.dbquery.constant.Constants.APP_ID;
import static com.github.wgzhao.dbquery.constant.Constants.MAGIC_SIGN;
import static com.github.wgzhao.dbquery.constant.Constants.SIGN;
import static com.github.wgzhao.dbquery.constant.Constants.WHITE_IP_LIST;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class DBQueryController {

    private final DBQueryService queryService;

    private final SignService signService;

    private final Environment environment;

    public DBQueryController(DBQueryService queryService, SignService signService, Environment environment) {
        this.queryService = queryService;
        this.signService = signService;
        this.environment = environment;
    }

    private Map<String, String> queryParams;
    private Map<String, String> controlParams;

    private String errorMsg;

    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }

    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public QueryResult executeQuery(@RequestParam() String selectId, @RequestParam Map<String, String> allParams,
                                    HttpServletRequest request, HttpServletResponse response) {

        log.info("Begin to query with selectId {} and all params {}", selectId, allParams);
        //take the all params break into 2 parts, one is the query params, the other is the control params
        this.queryParams = ParamUtil.getQueryParams(allParams);
        this.controlParams = ParamUtil.getControlParams(allParams);

        if (!checkControlParams(request)) {
            log.error("Control params is invalid, error msg is {}", errorMsg);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new QueryResult(errorMsg);
        }

        // convert all query params key to lower case
        Map<String, String> lowerQueryParams = ParamUtil.lowercaseParams(queryParams);
        try {
            QueryResult queryResult = new QueryResult();
            List<Map<String, Object>> result = queryService.query(selectId, controlParams.get(APP_ID), lowerQueryParams);
            queryResult.setStatus(200);
            queryResult.setSuccess(true);
            queryResult.setMessage("success");
            queryResult.setTotal(result.size());
            queryResult.setData(Map.of("result", result));
            return queryResult;
        } catch (ParamException e) {
            e.printStackTrace();
            log.error("Query failed, error msg is {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new QueryResult(e.getMessage());
        } catch (ClassNotFoundException | SQLException | RuntimeException e) {
            e.printStackTrace();
            log.error("Query failed, error msg is {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new QueryResult(e.getMessage());
        }
    }

    /**
     * check the control params is valid or not
     *
     * @return true if valid, false if not
     */
    private boolean checkControlParams(HttpServletRequest request) {
        String clientIP = HttpUtil.getClientIpAddr(request);
        log.info("client come from {}", clientIP);
        if (WHITE_IP_LIST.contains(clientIP)) {
            log.info("client ip {} is in white ip list, DO NOT check sign", clientIP);
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
        } else {
            // check _appId is existing or not
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
