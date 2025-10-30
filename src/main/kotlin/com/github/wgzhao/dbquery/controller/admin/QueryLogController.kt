package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.entities.QueryLog
import com.github.wgzhao.dbquery.repo.QueryLogRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/query-logs")
class QueryLogController {
    @Autowired
    private val queryLogRepo: QueryLogRepo? = null

    @GetMapping
    fun list(
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog?> {
        val pageable = createPageable(page, size, sortKey, sortOrder)
        return queryLogRepo!!.findAll(pageable)
    }

    @GetMapping("/search")
    fun search(
        @RequestParam(value = "q", required = true) q: String?,
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog?>? {
        val pageable = createPageable(page, size, sortKey, sortOrder)
        return queryLogRepo!!.findByQuerySqlContaining(q, pageable)
    }

    @GetMapping("/by/selectId/{selectId}")
    fun listBySelectId(
        @PathVariable("selectId") selectId: String?,
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog?>? {
        val pageable = createPageable(page, size, sortKey, sortOrder)
        return queryLogRepo!!.findAllBySelectId(selectId, pageable)
    }

    @GetMapping("/by/appId/{appId}")
    fun listByAppId(
        @PathVariable("appId") appId: String?,
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(value = "sortKey", required = false) sortKey: String,
        @RequestParam(value = "sortOrder", required = false) sortOrder: String
    ): Page<QueryLog?>? {
        val pageable = createPageable(page, size, sortKey, sortOrder)
        return queryLogRepo!!.findAllByAppId(appId, pageable)
    }

    private fun createPageable(page: Int, size: Int, sortKey: String, sortOrder: String): Pageable {
        var size = size
        val orders: MutableList<Sort.Order?> = ArrayList<Sort.Order?>()
        val sortBy: Sort?


        if (size < 0) {
            size = Int.Companion.MAX_VALUE
        }

        if (sortKey.isEmpty() || sortOrder.isEmpty()) {
            return Pageable.ofSize(size).withPage(page)
        }

        val k: Array<String?> = sortKey.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val o: Array<String?> = sortOrder.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (k.size != o.size) {
            return Pageable.ofSize(size).withPage(page)
        }

        if (k.size > 0) {
            for (i in k.indices) {
                if (o[i].equals("asc", ignoreCase = true)) {
                    orders.add(Sort.Order.asc(k[i]))
                } else {
                    orders.add(Sort.Order.desc(k[i]))
                }
            }
        }
        sortBy = Sort.by(orders)
        if (sortBy.isUnsorted()) {
            return Pageable.ofSize(size).withPage(page)
        } else {
            return PageRequest.of(page, size, sortBy)
        }
    }
}
