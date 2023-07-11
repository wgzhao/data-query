package com.lczq.dbquery.service.impl;

import com.lczq.dbquery.entities.DataSource;
import com.lczq.dbquery.entities.QueryConfig;
import com.lczq.dbquery.entities.QueryParams;
import com.lczq.dbquery.entities.QueryResult;
import com.lczq.dbquery.mapper.QueryMapper;
import com.lczq.dbquery.service.ConnectionDB;
import com.lczq.dbquery.service.DBQueryService;
import com.lczq.dbquery.service.MyPair;
import com.lczq.dbquery.util.CacheUtil;
import com.lczq.dbquery.util.ParamUtil;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public MyPair<String, QueryResult> query(String selectId, String appId, Map<String, String> lowerQueryParams)
    {
        QueryConfig queryConfig = queryMapper.queryConfigBySelectId(selectId);
        String redisKey = selectId + "_" + ParamUtil.crc32(lowerQueryParams);
        if (queryConfig == null) {
            logger.warn("The query selectId {} has not found", selectId);
            return new MyPair<>("查询ID " + selectId + " 不存在", null);
        }
        if (! queryConfig.isEnable()) {
            logger.warn("The query selectId {} has be disabled", selectId);
            return new MyPair<>("查询ID " + selectId + " 被禁用", null);
        }
        if (queryConfig.isEnableCache() && queryConfig.getCacheTime() > 0) {
            // try to get data from cache
            if (cacheUtil.exists(redisKey)) {
                logger.info("The result has retrieved from cache with key: {}", redisKey);
                return new MyPair<>("success", (QueryResult) cacheUtil.get(redisKey));
            }
        }
        List<QueryParams> queryParamsList = queryMapper.queryParamsBySelectId(selectId);
        Map<String, String> valuesMap = new HashMap<>();
        // 所有写入 query_params 表的参数都是必选参数，其他从 SQL 语句提取出来的参数为可选参数，同时 allParams 中的 key 也是小写
        for (QueryParams queryParams : queryParamsList) {
            // query_params 表中的参数名在写入时已经变成小写
            if (! lowerQueryParams.containsKey(queryParams.getParamName())) {
                return new MyPair<>("参数 " + queryParams.getParamName() + " 不存在", null);
            }
            valuesMap.put(queryParams.getParamName(), lowerQueryParams.get(queryParams.getParamName()));
        }
        // 提取 SQL 语句中的参数，参数是 ${xxx} 形式
        List<String> otherParams = getParamNames(queryConfig.getQuerySql());
        for (String paramName : otherParams) {
            valuesMap.put(paramName, lowerQueryParams.getOrDefault(paramName.toLowerCase(), ""));
        }
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String executeSql = sub.replace(queryConfig.getQuerySql());

        DataSource dataSource = queryMapper.queryDataSourceBySourceName(queryConfig.getDataSource());
        //fill back with real execute sql statement
        logger.info("execute sql is {}", executeSql);
        //async save executed sql to db
        queryMapper.saveQuerySql(appId, selectId, executeSql);
        queryConfig.setQuerySql(executeSql);
        QueryResult rsList;
        try {
            rsList = connectionDB.executeSQL(queryConfig, dataSource);
        }
        catch (Exception e) {
            logger.error("The query with selectId {} has failure: {}", selectId, e);
            return new MyPair<>("查询失败，请查看服务日志", null);
        }
        if (rsList == null || rsList.getResult() == null) {
            return new MyPair<>("未知异常", null);
        }
        else {
            // need write to cache or not ?
            if (queryConfig.isEnableCache()) {
                cacheUtil.set(redisKey, rsList, queryConfig.getCacheTime());
            }
            return new MyPair<>("success", rsList);
        }
    }

    private List<String> getParamNames(String sql)
    {
        List<String> result = new ArrayList<>();
        // all variable define are formed alphabetic, digits, underscore.
        String regex = "\\$\\{(?<name>\\w+?)\\}";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.add(matcher.group(i));
            }
        }
        return result;
    }
}
