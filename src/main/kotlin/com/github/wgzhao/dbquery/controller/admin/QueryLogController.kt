package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.entities.QueryLog
import com.github.wgzhao.dbquery.repo.QueryLogRepo
import com.github.wgzhao.dbquery.service.QueryLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/query-logs")
class QueryLogController(private val queryLogService: QueryLogService) {


    @GetMapping
    fun list(
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int?,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int?,
        @RequestParam(value = "sortKey", required = false) sortKey: String?,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String?
    ): Page<QueryLog>? {
        return queryLogService.findAll(page, size, sortKey, sortOrder)
    }

    @GetMapping("/search")
    fun search(
        @RequestParam(value = "q", required = true) q: String?,
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog>? {
        return queryLogService.findByQuerySqlContaining(q, page, size, sortKey, sortOrder)
    }

    @GetMapping("/by/selectId/{selectId}")
    fun listBySelectId(
        @PathVariable("selectId") selectId: String,
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog>? {
        return queryLogService.findAllBySelectId(selectId, page, size, sortKey, sortOrder)
    }

    @GetMapping("/by/appId/{appId}")
    fun listByAppId(
        @PathVariable("appId") appId: String,
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog>? {
        return queryLogService.findAllByAppId(appId, page, size, sortKey, sortOrder)
    }


}
