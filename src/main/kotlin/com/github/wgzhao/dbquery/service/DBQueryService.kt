package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.entities.QueryLog
import com.github.wgzhao.dbquery.errors.ParamException
import com.github.wgzhao.dbquery.repo.DataSourceRepo
import com.github.wgzhao.dbquery.repo.QueryConfigRepo
import com.github.wgzhao.dbquery.repo.QueryLogRepo
import com.github.wgzhao.dbquery.util.CacheUtil
import com.github.wgzhao.dbquery.util.ParamUtil
import org.apache.commons.text.StringSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class DBQueryService(
    val dataSourceRepo: DataSourceRepo,
    val queryConfigRepo: QueryConfigRepo,
    val queryLogRepo: QueryLogRepo,
    val connectionDB: ConnectionDB,
    val cacheUtil: CacheUtil,
) {
    val logger: Logger = LoggerFactory.getLogger(DBQueryService::class.java)


    fun query(
        selectId: String,
        appId: String,
        lowerQueryParams: MutableMap<String, String>?
    ): List<Map<String, Any>>? {
        val queryConfig = queryConfigRepo.findById(selectId).orElse(null)
        //        QueryResult queryResult = new QueryResult();
        if (queryConfig == null) {
            logger.warn("The query selectId {} has not found", selectId)
            throw ParamException("查询ID " + selectId + " 不存在")
        }

        if (!queryConfig.isEnable) {
            logger.warn("The query selectId {} has be disabled", selectId)
            throw ParamException("查询ID " + selectId + " 被禁用")
        }
        val redisKey = selectId + "_" + ParamUtil.crc32(lowerQueryParams)
        if (queryConfig.enableCache && queryConfig.cacheTime > 0) {
            // try to get data from cache
            if (cacheUtil.exists(redisKey)) {
                logger.info("The result has retrieved from cache with key: {}", redisKey)
                @Suppress("UNCHECKED_CAST")
                return cacheUtil.get(redisKey) as List<Map<String, Any>>?
            }
        }
        val executeSql = handleSql(queryConfig.querySql, lowerQueryParams)

        val dataSource = dataSourceRepo.findById(queryConfig.dataSource).orElse(null)
        if (dataSource == null) {
            logger.warn("The query selectId {} has not found data source: {}", selectId, queryConfig.dataSource)
            throw ParamException("查询ID " + selectId + " 未找到数据源")
        }
        //fill back with real execute sql statement
        logger.info("execute sql is:\n {}", executeSql)
        //async save executed sql to db
        queryLogRepo.save(QueryLog(0, appId, selectId, executeSql))
        queryConfig.querySql = executeSql
        try {
            val rsList = connectionDB.executeSQL(queryConfig, dataSource)
            if (queryConfig.enableCache) {
                cacheUtil.set(redisKey, (rsList as java.io.Serializable?)!!, queryConfig.cacheTime.toLong())
            }
            @Suppress("UNCHECKED_CAST")
            return rsList as List<Map<String, Any>>?
        } catch (e: Exception) {
            logger.error("The query with selectId {} has failure: {}", selectId, e.message)
            throw RuntimeException("查询失败: " + e.message)
        }
    }

    private fun getParamNames(sql: String): MutableList<String> {
        val result: MutableList<String> = ArrayList<String>()
        // all variable define are formed alphabetic, digits, underscore.
        val regex = "\\$\\{(?<name>\\w+?)\\}"
        val pattern = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher = pattern.matcher(sql)
        while (matcher.find()) {
            for (i in 1..matcher.groupCount()) {
                result.add(matcher.group(i))
            }
        }
        return result
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
    private fun handleSql(sql: String, lowerQueryParams: MutableMap<String, String>?): String {
        // 获取所有的参数
        val sqlParams = getParamNames(sql)
        if (sqlParams.isEmpty()) {
            return sql
        }
        val valueMap: MutableMap<String, Any?> = HashMap<String, Any?>()
        for (param in sqlParams) {
            valueMap[param] = lowerQueryParams?.getOrDefault(param.lowercase(Locale.getDefault()), "null")
        }
        return StringSubstitutor(valueMap).replace(sql)
    }
}
