package com.lczq.dbquery.service.impl;

import com.lczq.dbquery.entities.DataSource;
import com.lczq.dbquery.entities.QueryConfig;
import com.lczq.dbquery.entities.QueryParams;
import com.lczq.dbquery.entities.QueryResult;
import com.lczq.dbquery.mapper.QueryMapper;
import com.lczq.dbquery.service.ConnectionDB;
import com.lczq.dbquery.service.DBQueryService;
import com.lczq.dbquery.util.CacheUtil;
import com.lczq.dbquery.util.ParamUtil;
import javafx.util.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private CacheUtil cacheUtil;

    @Override
    public Pair<String, QueryResult> query(String selectId, Map<String, String> allParams)
    {
        QueryConfig queryConfig = queryMapper.queryConfigBySelectId(selectId);
        if (queryConfig == null) {
            logger.warn("The query selectId {} has not found", selectId);
            return new Pair<>("查询ID " + selectId + " 不存在", null);
        }
        if (! queryConfig.isEnable()) {
            logger.warn("The query selectId {} has be disabled", selectId);
            return new Pair<>("查询ID " + selectId + " 被禁用", null);
        }
        if (queryConfig.isEnableCache() && queryConfig.getCacheTime() > 0) {
            // try to get data from cache
            String redisKey = ParamUtil.generateKey(allParams);
            if (cacheUtil.exists(redisKey)) {
                logger.info("The result has retrieved from cache with key: {}", redisKey);
                return new Pair<>("success", (QueryResult) cacheUtil.get(redisKey));
            }
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
        QueryResult rsList;
        try {
            rsList = connectionDB.executeSQL(queryConfig, dataSource);
        }
        catch (Exception e) {
            logger.error("The query with selectId {} has failure: {}", selectId, e);
            return new Pair<>("查询失败，请查看服务日志", null);
        }
        if (rsList == null) {
            return new Pair<>("未知异常", null);
        }
        else {
            // need write to cache or not ?
            if (queryConfig.isEnableCache()) {
                cacheUtil.set(ParamUtil.generateKey(allParams), rsList, queryConfig.getCacheTime());
            }
            return new Pair<>("success", rsList);
        }
    }
}
