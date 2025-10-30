package com.github.wgzhao.dbquery.service.impl

import com.github.wgzhao.dbquery.entities.QueryLog
import com.github.wgzhao.dbquery.errors.ParamException
import com.github.wgzhao.dbquery.repo.DataSourceRepo
import com.github.wgzhao.dbquery.repo.QueryConfigRepo
import com.github.wgzhao.dbquery.repo.QueryLogRepo
import com.github.wgzhao.dbquery.service.ConnectionDB
import com.github.wgzhao.dbquery.service.DBQueryService
import com.github.wgzhao.dbquery.util.CacheUtil
import com.github.wgzhao.dbquery.util.ParamUtil
import jakarta.annotation.Resource
import org.apache.commons.text.StringSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class DBQueryServiceImpl

    : DBQueryService {
    var logger: Logger = LoggerFactory.getLogger(DBQueryServiceImpl::class.java)

    @Autowired
    private val dataSourceRepo: DataSourceRepo? = null

    @Autowired
    private val queryConfigRepo: QueryConfigRepo? = null

    @Autowired
    private val queryLogRepo: QueryLogRepo? = null

    @Autowired
    private val connectionDB: ConnectionDB? = null

    @Resource
    private val cacheUtil: CacheUtil? = null


    override fun query(
        selectId: String,
        appId: String?,
        lowerQueryParams: MutableMap<String?, String?>
    ): MutableList<MutableMap<String?, Any?>?>? {
        val queryConfig = queryConfigRepo!!.findById(selectId).orElse(null)
        val redisKey = selectId + "_" + ParamUtil.crc32(lowerQueryParams)
        //        QueryResult queryResult = new QueryResult();
        if (queryConfig == null) {
            logger.warn("The query selectId {} has not found", selectId)
            throw ParamException("查询ID " + selectId + " 不存在")
        }
        if (!queryConfig.isEnable()) {
            logger.warn("The query selectId {} has be disabled", selectId)
            throw ParamException("查询ID " + selectId + " 被禁用")
        }
        if (queryConfig.isEnableCache() && queryConfig.getCacheTime() > 0) {
            // try to get data from cache
            if (cacheUtil!!.exists(redisKey)) {
                logger.info("The result has retrieved from cache with key: {}", redisKey)
                return cacheUtil.get(redisKey) as MutableList<MutableMap<String?, Any?>?>?
            }
        }
        val executeSql = handleSql(queryConfig.getQuerySql(), lowerQueryParams)

        val dataSource = dataSourceRepo!!.findById(queryConfig.getDataSource()).orElse(null)
        if (dataSource == null) {
            logger.warn("The query selectId {} has not found data source: {}", selectId, queryConfig.getDataSource())
            throw ParamException("查询ID " + selectId + " 未找到数据源")
        }
        //fill back with real execute sql statement
        logger.info("execute sql is:\n {}", executeSql)
        //async save executed sql to db
        queryLogRepo!!.save<QueryLog?>(QueryLog(0, appId, selectId, executeSql))
        queryConfig.setQuerySql(executeSql)
        try {
            val rsList = connectionDB!!.executeSQL(queryConfig, dataSource)
            if (queryConfig.isEnableCache()) {
                cacheUtil!!.set(redisKey, (rsList as java.io.Serializable?)!!, queryConfig.getCacheTime().toLong())
            }
            return rsList
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
    private fun handleSql(sql: String, lowerQueryParams: MutableMap<String?, String?>): String? {
        // 获取所有的参数
        val sqlParams = getParamNames(sql)
        if (sqlParams.isEmpty()) {
            return sql
        }
        val valueMap: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        for (param in sqlParams) {
            valueMap.put(param, lowerQueryParams.getOrDefault(param.lowercase(Locale.getDefault()), "null"))
        }
        return StringSubstitutor(valueMap).replace(sql)
    }
}
