package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.DbSourceDto
import com.github.wgzhao.dbquery.dto.QueryResult
import com.github.wgzhao.dbquery.entities.QueryConfig
import com.github.wgzhao.dbquery.entities.QueryParam
import com.github.wgzhao.dbquery.repo.DataSourceRepo
import com.github.wgzhao.dbquery.repo.QueryConfigRepo
import com.github.wgzhao.dbquery.repo.QueryParamRepo
import com.github.wgzhao.dbquery.service.ConnectionDB
import com.github.wgzhao.dbquery.util.CacheUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.sql.SQLException
import org.slf4j.LoggerFactory

/**
 * QueryConfigController
 * manipulate for [QueryConfig] table
 */
@RestController
@RequestMapping("\${app.api.manage-prefix}/query-configs")
class QueryConfigController {
    private val logger = LoggerFactory.getLogger(QueryConfigController::class.java)

    @Autowired
    private val queryConfigRepo: QueryConfigRepo? = null

    @Autowired
    private val dataSourceRepo: DataSourceRepo? = null

    @Autowired
    private val queryParamsRepo: QueryParamRepo? = null

    @Autowired
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
        return queryConfigRepo!!.save(queryConfig)
    }

    @GetMapping("/params/{selectId}")
    fun listParams(@PathVariable("selectId") selectId: String?): MutableList<QueryParam?>? {
        return queryParamsRepo!!.findBySelectId(selectId)
    }

    @PutMapping("/params")
    fun saveParams(@RequestBody params: MutableList<QueryParam?>): Int {
        queryParamsRepo!!.saveAll(params)
        return params.size
    }

    @DeleteMapping("/cache/{selectId}")
    fun deleteCache(@PathVariable("selectId") selectId: String?): Int {
        return cacheUtil!!.deleteKeys(selectId)
    }

    @PostMapping("/testQuery")
    fun testQuery(@RequestBody payload: MutableMap<String?, String>): QueryResult {
        val sourceId: String = payload["sourceId"]!!
        val querySql = payload["querySql"]
        val dataSource = dataSourceRepo!!.findById(sourceId).orElse(null)

        if (dataSource == null) {
            return QueryResult.error(400, "数据源不存在")
        }
        try {
            val rsList = connectionDB!!.executeSQL(querySql, dataSource)
            // normalize to List<Map<String, Any>>
            val normalized: List<Map<String, Any>> = rsList.mapNotNull { row ->
                row?.entries?.mapNotNull { (k, v) -> k?.let { it to (v as Any) } }?.toMap()
            }
            return QueryResult.success(normalized)
        } catch (e: SQLException) {
            logger.error("test query failed: {}", e.message)
            return QueryResult.error(400, e.message)
        } catch (e: ClassNotFoundException) {
            logger.error("test query failed: {}", e.message)
            return QueryResult.error(400, e.message)
        }
    }
}
