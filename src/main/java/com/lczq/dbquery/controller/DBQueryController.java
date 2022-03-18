package com.lczq.dbquery.controller;

import com.lczq.dbquery.entities.QueryResult;
import com.lczq.dbquery.entities.SignEntity;
import com.lczq.dbquery.param.RestResponse;
import com.lczq.dbquery.param.RestResponseBuilder;
import com.lczq.dbquery.service.DBQueryService;
import com.lczq.dbquery.service.MyPair;
import com.lczq.dbquery.service.SignService;
import com.lczq.dbquery.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

import static com.lczq.dbquery.constant.Constants.MAGIC_SIGN;


@RestController
@RequestMapping(value = "/api/v1")
public class DBQueryController
{

    private static Logger logger = LoggerFactory.getLogger(DBQueryController.class);

    @Autowired
    private DBQueryService queryService;

    @Autowired
    private SignService signService;

    @GetMapping("/")
    public String index()
    {
        return "Hello, World!";
    }

    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public RestResponse executeQuery(@RequestParam() String selectId, @RequestParam Map<String, String> allParams)
    {
        logger.info("Begin to query with selectId {} and params {}", selectId, allParams);
        // check _sign has exists
        String sign = allParams.getOrDefault("_sign", null);
        if (sign == null || sign.isEmpty()) {
            return RestResponseBuilder.fail(400, "签名不存在");
        }

        if (MAGIC_SIGN.equals(sign)) {
            // it's magic ,skip all validation
            //TODO: magic sign should be configurable, and ONLY can be used in test env
            //
        } else {
            // check _appId is exists or not
            String appId = allParams.getOrDefault("_appId", null);
            if (appId == null || appId.isEmpty()) {
                return RestResponseBuilder.fail(400, "缺少必要的参数 _appId");
            }

            SignEntity signEntity = signService.querySign(appId);

            if (signEntity == null || signEntity.getAppKey() == null) {
                return RestResponseBuilder.fail(400, "无效的 _appId");
            }

            System.out.printf("appKey = %s%n", signEntity.getAppKey());
            if (!SignUtil.validSign(allParams, signEntity)) {
                return RestResponseBuilder.fail(400, "无效签名");
            }
        }

        MyPair<String, QueryResult> result = queryService.query(selectId, allParams);
        if (result.getSecond() == null || result.getSecond().getResult() == null) {
            return RestResponseBuilder.fail(result.getFirst());
        }
        else {
            return RestResponseBuilder.succ((long) result.getSecond().getResult().size(), result.getSecond());
        }
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
