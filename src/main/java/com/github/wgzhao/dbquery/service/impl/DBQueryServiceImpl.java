package com.github.wgzhao.dbquery.service.impl;

import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import com.github.wgzhao.dbquery.entities.QueryLog;
import com.github.wgzhao.dbquery.errors.ParamException;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import com.github.wgzhao.dbquery.repo.QueryLogRepo;
import com.github.wgzhao.dbquery.service.ConnectionDB;
import com.github.wgzhao.dbquery.service.DBQueryService;
import com.github.wgzhao.dbquery.util.CacheUtil;
import com.github.wgzhao.dbquery.util.ParamUtil;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DBQueryServiceImpl
        implements DBQueryService {

    Logger logger = LoggerFactory.getLogger(DBQueryServiceImpl.class);

    @Autowired
    private DataSourceRepo dataSourceRepo;

    @Autowired
    private QueryConfigRepo queryConfigRepo;

    @Autowired
    private QueryLogRepo queryLogRepo;

    @Autowired
    private ConnectionDB connectionDB;

    @Resource
    private CacheUtil cacheUtil;


    @Override
    public List<Map<String, Object>> query(String selectId, String appId, Map<String, String> lowerQueryParams) {
        QueryConfig queryConfig = queryConfigRepo.findById(selectId).orElse(null);
        String redisKey = selectId + "_" + ParamUtil.crc32(lowerQueryParams);
//        QueryResult queryResult = new QueryResult();
        if (queryConfig == null) {
            logger.warn("The query selectId {} has not found", selectId);
            throw new ParamException("查询ID " + selectId + " 不存在");
        }
        if (!queryConfig.isEnable()) {
            logger.warn("The query selectId {} has be disabled", selectId);
            throw new ParamException("查询ID " + selectId + " 被禁用");
        }
        if (queryConfig.isEnableCache() && queryConfig.getCacheTime() > 0) {
            // try to get data from cache
            if (cacheUtil.exists(redisKey)) {
                logger.info("The result has retrieved from cache with key: {}", redisKey);
                return (List<Map<String, Object>>) cacheUtil.get(redisKey);
            }
        }
        String executeSql = handleSql(queryConfig.getQuerySql(), lowerQueryParams);

        DataSources dataSource = dataSourceRepo.findById(queryConfig.getDataSource()).orElse(null);
        //fill back with real execute sql statement
        logger.info("execute sql is {}", executeSql);
        //async save executed sql to db
        queryLogRepo.save(new QueryLog(0, appId, selectId, executeSql));
        queryConfig.setQuerySql(executeSql);
        try {
            List<Map<String, Object>> rsList = connectionDB.executeSQL(queryConfig, dataSource);
            if (queryConfig.isEnableCache()) {
                cacheUtil.set(redisKey, (Serializable) rsList, queryConfig.getCacheTime());
            }
            return rsList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("The query with selectId {} has failure: {}", selectId, e.getMessage());
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    private List<String> getParamNames(String sql) {
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

    /**
     * 将 SQL 中的 ${xxx} 形式的参数替换为实际的值
     * 替换规则如下：
     * 1. 如果变量${xxx} 已经在 query_params 表中定义，则直接替换为定义的值，否则
     * 2. 变量使用 null 字符串进行替换
     * @param sql 需要替换的 SQL
     * @param lowerQueryParams 传递过来的所有的参数
     * @return 替换后的 SQL
     */
    private String handleSql(String sql, Map<String, String> lowerQueryParams) {
        // 获取所有的参数
        List<String> sqlParams = getParamNames(sql);
        if (sqlParams.isEmpty()) {
            return sql;
        }
        Map<String, Object> valueMap = new HashMap<>();
        for (String param: sqlParams) {
            valueMap.put(param, lowerQueryParams.getOrDefault(param.toLowerCase(), "null"));
        }
        return new StringSubstitutor(valueMap).replace(sql);
    }
}
