package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.DbSourceDto;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import com.github.wgzhao.dbquery.entities.QueryParam;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import com.github.wgzhao.dbquery.repo.QueryParamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin/api/v1/queryconfig")
public class QueryConfigController {

    @Autowired
    private QueryConfigRepo queryConfigRepo;

    @Autowired
    private DataSourceRepo dataSourceRepo;

    @Autowired
    private QueryParamRepo queryParamsRepo;

    @GetMapping
    public List<QueryConfig> list() {
        return queryConfigRepo.findAll();
    }

    @GetMapping("/{id}")
    public QueryConfig get(@PathVariable("id") String id) {
        return queryConfigRepo.findById(id).orElse(null);
    }

    @GetMapping("/datasources")
    public Collection<DbSourceDto> listDataSources() {
        return dataSourceRepo.findNoAndName();
    }

    @PostMapping
    public QueryConfig save(@RequestBody  QueryConfig queryConfig) {
        return queryConfigRepo.save(queryConfig);
    }

    @GetMapping("/params/{selectId}")
    public List<QueryParam> listParams(@PathVariable("selectId") String selectId) {
        return queryParamsRepo.findBySelectId(selectId);
    }

    @PutMapping("/params")
    public int saveParams(@RequestBody  List<QueryParam> params) {
        queryParamsRepo.saveAll(params);
        return params.size();
    }
}
