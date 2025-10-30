package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.ApiResponse
import com.github.wgzhao.dbquery.entities.DataSources
import com.github.wgzhao.dbquery.repo.DataSourceRepo
import com.github.wgzhao.dbquery.repo.QueryConfigRepo
import com.github.wgzhao.dbquery.util.DbUtil
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/data-sources")
@RequiredArgsConstructor
class DataSourcesController {
    private val dataSourceRepo: DataSourceRepo? = null

    private val queryConfigRepo: QueryConfigRepo? = null // Add this


    @GetMapping
    fun list(): MutableList<DataSources?> {
        return dataSourceRepo!!.findAll()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): DataSources? {
        return dataSourceRepo!!.findById(id).orElse(null)
    }

    @PostMapping("/test-connection")
    fun testConnection(@RequestBody db: DataSources): ApiResponse<String?> {
        val res = DbUtil.testConnect(db)
        if (res == "success") {
            return ApiResponse.Companion.success<String?>("Connection successful")
        } else {
            return ApiResponse.Companion.error<String?>(500, res)
        }
    }

    @PostMapping
    fun save(@RequestBody db: DataSources): DataSources {
        return dataSourceRepo!!.save<DataSources>(db)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResponse<String?> {
        // check if it has some query config associated with this data sources
        if (queryConfigRepo!!.existsByDataSource(id)) {
            return ApiResponse.Companion.error<String?>(
                400,
                "Cannot delete: Data source is used by one or more query configurations"
            )
        }
        if (dataSourceRepo!!.existsById(id)) {
            dataSourceRepo.deleteById(id)
        }
        return ApiResponse.Companion.success<String?>(null)
    }
}
