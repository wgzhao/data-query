package com.github.wgzhao.dbquery.service.impl;

import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import com.github.wgzhao.dbquery.entities.QueryLog;
import com.github.wgzhao.dbquery.entities.QueryParam;
import com.github.wgzhao.dbquery.dto.QueryResult;
import com.github.wgzhao.dbquery.errors.ParamException;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import com.github.wgzhao.dbquery.repo.QueryLogRepo;
import com.github.wgzhao.dbquery.repo.QueryParamRepo;
import com.github.wgzhao.dbquery.service.ConnectionDB;
import com.github.wgzhao.dbquery.service.MyPair;
import com.github.wgzhao.dbquery.service.DBQueryService;
import com.github.wgzhao.dbquery.util.CacheUtil;
import com.github.wgzhao.dbquery.util.ParamUtil;
import jakarta.annotation.Resource;
import org.apache.commons.text.StringSubstitutor;
import org.hibernate.QueryParameterException;
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
        implements DBQueryService
{

    Logger logger = LoggerFactory.getLogger(DBQueryServiceImpl.class);

    @Autowired
    private DataSourceRepo dataSourceRepo;

    @Autowired
    private QueryConfigRepo queryConfigRepo;

    @Autowired
    private QueryParamRepo queryParamsRepo;

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
        List<QueryParam> queryParamList = queryParamsRepo.findBySelectId(selectId);
        Map<String, String> valuesMap = new HashMap<>();
        // 所有写入 query_params 表的参数都是必选参数，其他从 SQL 语句提取出来的参数为可选参数，同时 allParams 中的 key 也是小写
        for (QueryParam queryParam : queryParamList) {
            // query_params 表中的参数名在写入时已经变成小写
            if (!lowerQueryParams.containsKey(queryParam.getParamName())) {
                logger.warn("The query param {} has not found", queryParam.getParamName());
                throw new ParamException("参数 " + queryParam.getParamName() + " 不存在");
            }
            valuesMap.put(queryParam.getParamName(), lowerQueryParams.get(queryParam.getParamName()));
        }
        // 提取 SQL 语句中的参数，参数是 ${xxx} 形式
        List<String> otherParams = getParamNames(queryConfig.getQuerySql());
        for (String paramName : otherParams) {
            valuesMap.put(paramName, lowerQueryParams.getOrDefault(paramName.toLowerCase(), ""));
        }
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String executeSql = sub.replace(queryConfig.getQuerySql());

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
        } catch (ClassNotFoundException e) {
            logger.error("The query with selectId {} has failure: {}", selectId, e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        } catch (SQLException e) {
            logger.error("The query with selectId {} has failure: {}", selectId, e);
            throw new RuntimeException("查询的 SQL语句异常: " + e.getMessage());
        } catch (Exception e) {
            logger.error("The query with selectId {} has failure: {}", selectId, e);
            throw new RuntimeException("查询失败: " + e.getMessage());
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
