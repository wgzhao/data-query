package com.lczq.dbquery.controller;

import com.lczq.dbquery.entities.QueryConfig;
import com.lczq.dbquery.entities.QueryParams;
import com.lczq.dbquery.entities.SignEntity;
import com.lczq.dbquery.mapper.QueryMapper;
import com.lczq.dbquery.param.RestResponse;
import com.lczq.dbquery.param.RestResponseBuilder;
import com.lczq.dbquery.service.SignService;
import com.lczq.dbquery.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

@RestController
@RequestMapping("/mgt")
public class DBMgtController
{

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private SignService signService;

    @Resource
    private CacheUtil cacheUtil;

    @GetMapping("/queryconfig/{selectId}")
    public QueryConfig getQueryConfig(@PathVariable String selectId)
    {
        return queryMapper.queryConfigBySelectId(selectId);
    }

    @GetMapping("/queryconfig/exists/{selectId}")
    public boolean existsQueryConfig(@PathVariable String selectId)
    {
        return queryMapper.queryConfigBySelectId(selectId) != null;
    }

    @PostMapping(value = "/queryconfig", consumes = "application/json")
    public RestResponse<String> saveQueryConfig(@RequestBody QueryConfig queryConfig)
    {
        queryMapper.saveQueryConfig(queryConfig);
        return new RestResponse<>(200, "success");
    }

    @DeleteMapping("/queryconfig/{selectId}")
    public RestResponse<String> deleteQueryConfig(@PathVariable String selectId)
    {
        queryMapper.deleteQueryConfig(selectId);
        queryMapper.deleteQueryParams(selectId);
        return new RestResponse<>(200, "success");
    }

    @GetMapping("/queryparams/{selectId}")
    public List<QueryParams> getQueryParams(@PathVariable String selectId)
    {
        return queryMapper.queryParamsBySelectId(selectId);
    }

    @PostMapping(value = "/queryparams", consumes = "application/json")
    public RestResponse<String> saveQueryParams(@RequestBody List<QueryParams> queryParams)
    {
        for (QueryParams qp : queryParams) {
            queryMapper.saveQueryParams(qp);
        }
        return new RestResponse<>(200, "success");
    }

    @DeleteMapping("/queryparams/{selectId}")
    public RestResponse<String> deleteQueryParams(@PathVariable String selectId)
    {
        queryMapper.deleteQueryParams(selectId);
        return new RestResponse<>(200, "success");
    }

    @PostMapping(value = "/gen_sign/{applier}", produces = "application/json;charset=UTF-8")
    public SignEntity genSign(@PathVariable String applier, @RequestHeader MultiValueMap<String, String> headers)
    {
        if (headers.isEmpty() || !headers.containsKey("secret")) {
            return null;
        }
        if (!"lczq".equals(headers.getFirst("secret"))) {
            return null;
        }
        if (null == applier || applier.isEmpty()) {
            return null;
        }
        return signService.genSign(applier);
    }

    @DeleteMapping(value = "/delete_redis_keys/{selectId}", produces = "application/json")
    public RestResponse<String> deleteRedisKeys(@PathVariable String selectId)
    {
        int deleteNum;
        deleteNum = cacheUtil.deleteKeys(selectId);
        return RestResponseBuilder.succ((long) deleteNum, "");
    }
}
