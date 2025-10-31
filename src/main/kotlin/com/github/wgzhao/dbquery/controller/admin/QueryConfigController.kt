package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.DbSourceDto
import com.github.wgzhao.dbquery.dto.QueryResult
import com.github.wgzhao.dbquery.entities.QueryConfig
import com.github.wgzhao.dbquery.service.ConnectionDB
import com.github.wgzhao.dbquery.service.DataSourceService
import com.github.wgzhao.dbquery.service.QueryConfigService
import com.github.wgzhao.dbquery.util.CacheUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.*
import java.sql.SQLException

/**
 * QueryConfigController
 * manipulate for [QueryConfig] table
 */
@RestController
@RequestMapping("\${app.api.manage-prefix}/query-configs")
class QueryConfigController(
    val queryConfigService: QueryConfigService,
    val dataSourceService: DataSourceService,
    val cacheUtil: CacheUtil,
    val connectionDB: ConnectionDB
) {
    private val logger = KotlinLogging.logger {}

    /**
     * list all query config
     * @return list of [QueryConfig]
     */
    @GetMapping
    fun list(): MutableList<QueryConfig?> {
        return queryConfigService.getAll()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): QueryConfig? {
        return queryConfigService.findById(id).orElse(null)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String) {
        queryConfigService.deleteById(id)
    }

    @GetMapping("/data-sources")
    fun listDataSources(): Collection<DbSourceDto> {
        return dataSourceService.findNoAndName()
    }

    @PostMapping
    fun save(@RequestBody queryConfig: QueryConfig): QueryConfig {
        return queryConfigService.save(queryConfig)
    }

    @DeleteMapping("/cache/{selectId}")
    fun deleteCache(@PathVariable("selectId") selectId: String?): Int {
        return cacheUtil.deleteKeys(selectId)
    }

    @PostMapping("/testQuery")
    fun testQuery(@RequestBody payload: MutableMap<String?, String>): QueryResult {
        val sourceId: String = payload["sourceId"]!!
        val querySql = payload["querySql"]
        val dataSource =
            dataSourceService.findById(sourceId).orElse(null) ?: return QueryResult.error(400, "数据源不存在")

        try {
            val rsList = connectionDB.executeSQL(querySql, dataSource)
            // normalize to List<Map<String, Any>>
            val normalized: List<Map<String, Any>> = rsList.mapNotNull { row ->
                row?.entries?.mapNotNull { (k, v) -> k?.let { it to (v as Any) } }?.toMap()
            }
            return QueryResult.success(normalized)
        } catch (e: SQLException) {
            logger.error { "${"test query failed: {}"} ${e.message}" }
            return QueryResult.error(400, e.message)
        } catch (e: ClassNotFoundException) {
            logger.error { "${"test query failed: {}"} ${e.message}" }
            return QueryResult.error(400, e.message)
        }
    }
}
