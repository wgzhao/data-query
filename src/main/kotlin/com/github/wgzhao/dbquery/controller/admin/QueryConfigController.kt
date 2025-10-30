package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.DbSourceDto
import com.github.wgzhao.dbquery.dto.QueryResult
import com.github.wgzhao.dbquery.entities.QueryConfig
import com.github.wgzhao.dbquery.repo.DataSourceRepo
import com.github.wgzhao.dbquery.repo.QueryConfigRepo
import com.github.wgzhao.dbquery.repo.QueryParamRepo
import com.github.wgzhao.dbquery.service.ConnectionDB
import com.github.wgzhao.dbquery.util.CacheUtil
import jakarta.annotation.Resource
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.sql.SQLException
import java.util.Map

/**
 * QueryConfigController
 * manipulate for [QueryConfig] table
 */
@RestController
@RequestMapping("\${app.api.manage-prefix}/query-configs")
@Slf4j
class QueryConfigController {
    @Autowired
    private val queryConfigRepo: QueryConfigRepo? = null

    @Autowired
    private val dataSourceRepo: DataSourceRepo? = null

    @Autowired
    private val queryParamsRepo: QueryParamRepo? = null

    @Resource
    private val cacheUtil: CacheUtil? = null

    @Autowired
    private val connectionDB: ConnectionDB? = null

    /**
     * list all query config
     * @return list of [QueryConfig]
     */
    @GetMapping
    fun list(): MutableList<QueryConfig?> {
        return queryConfigRepo!!.findAll()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): QueryConfig? {
        return queryConfigRepo!!.findById(id).orElse(null)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String) {
        queryConfigRepo!!.deleteById(id)
    }

    @GetMapping("/data-sources")
    fun listDataSources(): MutableCollection<DbSourceDto?>? {
        return dataSourceRepo!!.findNoAndName()
    }

    @PostMapping
    fun save(@RequestBody queryConfig: QueryConfig): QueryConfig {
        return queryConfigRepo!!.save<QueryConfig>(queryConfig)
    }

    @GetMapping("/params/{selectId}")
    fun listParams(@PathVariable("selectId") selectId: String?): MutableList<QueryParam?>? {
        return queryParamsRepo!!.findBySelectId(selectId)
    }

    @PutMapping("/params")
    fun saveParams(@RequestBody params: MutableList<QueryParam?>): Int {
        queryParamsRepo!!.saveAll<QueryParam?>(params)
        return params.size
    }

    @DeleteMapping("/cache/{selectId}")
    fun deleteCache(@PathVariable("selectId") selectId: String?): Int {
        return cacheUtil!!.deleteKeys(selectId)
    }

    @PostMapping("/testQuery")
    fun testQuery(@RequestBody payload: MutableMap<String?, String>): QueryResult {
        val sourceId: String = payload.get("sourceId")!!
        val querySql = payload.get("querySql")
        val dataSource = dataSourceRepo!!.findById(sourceId).orElse(null)
        val queryResult = QueryResult()
        if (dataSource == null) {
            queryResult.setStatus(400)
            queryResult.setMessage("数据源不存在")
            return queryResult
        }
        try {
            val rsList = connectionDB!!.executeSQL(querySql, dataSource)
            queryResult.setStatus(200)
            queryResult.setTotal(rsList.size)
            queryResult.setData(Map.of<String?, Any?>("result", rsList))
            return queryResult
        } catch (e: SQLException) {
            QueryConfigController.log.error("test query failed: {}", e.message)
            queryResult.setStatus(400)
            queryResult.setMessage(e.message)
            return queryResult
        } catch (e: ClassNotFoundException) {
            QueryConfigController.log.error("test query failed: {}", e.message)
            queryResult.setStatus(400)
            queryResult.setMessage(e.message)
            return queryResult
        }
    }
}
