package com.lczq.dbquery.controller;

import com.lczq.dbquery.param.RestResponse;
import com.lczq.dbquery.param.RestResponseBuilder;
import com.lczq.dbquery.service.DBQueryService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class DBQueryController
{

    @Autowired
    private DBQueryService queryService;

    @GetMapping("/")
    public String index()
    {
        return "Hello, World!";
    }

    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public RestResponse executeQuery(@RequestParam() String selectId, @RequestParam Map<String, String> allParams)
    {
        Pair<String, List<Map<String, Object>>> result = queryService.query(selectId, allParams);
        if (result.getValue() == null) {
            return RestResponseBuilder.fail(result.getKey());
        }
        else {
            return RestResponseBuilder.succ((long) result.getValue().size(), result.getValue());
        }
    }
}
