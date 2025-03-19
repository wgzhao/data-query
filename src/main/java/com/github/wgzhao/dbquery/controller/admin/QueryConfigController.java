package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.dto.DbSourceDto;
import com.github.wgzhao.dbquery.dto.QueryResult;
import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import com.github.wgzhao.dbquery.entities.QueryParam;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import com.github.wgzhao.dbquery.repo.QueryParamRepo;
import com.github.wgzhao.dbquery.service.ConnectionDB;
import com.github.wgzhao.dbquery.util.CacheUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * QueryConfigController
 * manipulate for {@link QueryConfig} table
 */
@RestController
@RequestMapping("${app.api.manage-prefix}/queryConfig")
@Slf4j
public class QueryConfigController {

    @Autowired
    private QueryConfigRepo queryConfigRepo;

    @Autowired
    private DataSourceRepo dataSourceRepo;

    @Autowired
    private QueryParamRepo queryParamsRepo;

    @Resource
    private CacheUtil cacheUtil;

    @Autowired
    private ConnectionDB connectionDB;

    /**
     * list all query config
     * @return list of {@link QueryConfig}
     */
    @GetMapping
    public List<QueryConfig> list() {
        return queryConfigRepo.findAll();
    }

    @GetMapping("/{id}")
    public QueryConfig get(@PathVariable("id") String id) {
        return queryConfigRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        queryConfigRepo.deleteById(id);
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

    @DeleteMapping("/cache/{selectId}")
    public int deleteCache(@PathVariable("selectId") String selectId) {
        return cacheUtil.deleteKeys(selectId);
    }

    @PostMapping("/testQuery")
    public QueryResult testQuery(@RequestBody Map<String, String> payload) {
        String sourceId = payload.get("sourceId");
        String querySql = payload.get("querySql");
        DataSources dataSource = dataSourceRepo.findById(sourceId).orElse(null);
        QueryResult queryResult = new QueryResult();
        if (dataSource == null) {
            queryResult.setStatus(400);
            queryResult.setMessage("数据源不存在");
            return queryResult;
        }
        try {
            List<Map<String, Object>> rsList = connectionDB.executeSQL(querySql, dataSource);
            queryResult.setStatus(200);
            queryResult.setTotal(rsList.size());
            queryResult.setData(Map.of("result", rsList));
            return queryResult;
        } catch (SQLException | ClassNotFoundException e) {
            log.error("test query failed: {}", e.getMessage());
            queryResult.setStatus(400);
            queryResult.setMessage(e.getMessage());
            return  queryResult;
        }
    }
}
