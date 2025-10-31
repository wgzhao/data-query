package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.ApiResponse
import com.github.wgzhao.dbquery.entities.DataSources
import com.github.wgzhao.dbquery.service.DataSourceService
import com.github.wgzhao.dbquery.service.QueryConfigService
import com.github.wgzhao.dbquery.util.DbUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/data-sources")
class DataSourcesController(
    private val dataSourceService: DataSourceService,
    private val queryConfigService: QueryConfigService
) {

    @GetMapping
    fun list(): List<DataSources?> {
        return dataSourceService.findAll()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): DataSources? {
        return dataSourceService.findById(id).orElse(null)
    }

    @PostMapping("/test-connection")
    fun testConnection(@RequestBody db: DataSources): ApiResponse<String?> {
        val res = DbUtil.testConnect(db)
        if (res == "success") {
            return ApiResponse.success<String?>("Connection successful")
        } else {
            return ApiResponse.error<String?>(500, res)
        }
    }

    @PostMapping
    fun save(@RequestBody db: DataSources): DataSources {
        return dataSourceService.save(db)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResponse<String?> {
        // check if it has some query config associated with this data sources
        if (queryConfigService.existsByDataSource(id)) {
            return ApiResponse.error<String?>(
                400,
                "Cannot delete: Data source is used by one or more query configurations"
            )
        }
        dataSourceService.deleteById(id)

        return ApiResponse.success<String?>(null)
    }
}
