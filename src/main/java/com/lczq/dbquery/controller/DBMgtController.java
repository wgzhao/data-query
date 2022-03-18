package com.lczq.dbquery.controller;

import com.lczq.dbquery.entities.QueryConfig;
import com.lczq.dbquery.entities.QueryParams;
import com.lczq.dbquery.mapper.QueryMapper;
import com.lczq.dbquery.param.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mgt")
public class DBMgtController
{

    @Autowired
    private QueryMapper queryMapper;

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

    @PostMapping(value="/queryconfig", consumes = "application/json")
    public RestResponse saveQueryConfig(@RequestBody QueryConfig queryConfig)
    {
        queryMapper.saveQueryConfig(queryConfig);
        return new RestResponse<>(200, "success");
    }


    @DeleteMapping("/queryconfig/{selectId}")
    public RestResponse deleteQueryConfig(@PathVariable String selectId)
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

    @PostMapping(value="/queryparams", consumes = "application/json")
    public RestResponse saveQueryParams(@RequestBody List<QueryParams> queryParams)
    {
        for( QueryParams qp: queryParams ) {
            queryMapper.saveQueryParams(qp);
        }
        return new RestResponse<>(200, "success");
    }

    @DeleteMapping("/queryparams/{selectId}")
    public RestResponse deleteQueryParams(@PathVariable String selectId)
    {
        queryMapper.deleteQueryParams(selectId);
        return new RestResponse<>(200, "success");
    }

}
