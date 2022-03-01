package com.lczq.dbquery.service.impl;

import com.lczq.dbquery.entities.DataSource;
import com.lczq.dbquery.entities.QueryConfig;
import com.lczq.dbquery.entities.QueryParams;
import com.lczq.dbquery.mapper.QueryMapper;
import com.lczq.dbquery.service.ConnectionDB;
import com.lczq.dbquery.service.DBQueryService;
import javafx.util.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DBQueryServiceImpl
        implements DBQueryService
{

    Logger logger = LoggerFactory.getLogger(DBQueryServiceImpl.class);

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private ConnectionDB connectionDB;

    @Override
    public Pair<String, List<Map<String, Object>>> query(String selectId, Map<String, String> allParams)
    {
        logger.info("query select_id: {}", selectId);
        QueryConfig queryConfig = queryMapper.queryConfigBySelectId(selectId);
        if (queryConfig == null) {
            logger.warn("query select_id: {} not found", selectId);
            return new Pair<>("查询ID " + selectId + " 不存在或已禁用", null);
        }
        List<QueryParams> queryParamsList = queryMapper.queryParamsBySelectId(selectId);
        Map<String, String> valuesMap = new HashMap<>();
        for (QueryParams queryParams : queryParamsList) {
            valuesMap.put(queryParams.getParamName(), allParams.getOrDefault(queryParams.getParamName(), null));
        }
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String executeSql = sub.replace(queryConfig.getQuerySql());

        DataSource dataSource = queryMapper.queryDataSourceBySourceName(queryConfig.getDataSource());
        //fill back with real execute sql statement
        queryConfig.setQuerySql(executeSql);
        List<Map<String, Object>> rsList;
        try {
            rsList = connectionDB.executeSQL(queryConfig, dataSource);
        }
        catch (Exception e) {
            logger.error("query with select_id {} error: ", selectId, e);
            return new Pair<>("查询失败，请查看服务日志", null);
        }
        if (rsList == null) {
            return new Pair<>("未知异常", null);
        }
        else {
            return new Pair<>("success", rsList);
        }
    }
}
