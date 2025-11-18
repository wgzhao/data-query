package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.entities.QueryLog
import com.github.wgzhao.dbquery.repo.QueryLogRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.text.split

@Service
class QueryLogService(private val queryLogRepo: QueryLogRepo) {

    fun findAll(page: Int?, size: Int?, sortKey: String?, sortOrder: String?): Page<QueryLog>? {
        return queryLogRepo.findAll(createPageable(page, size, sortKey, sortOrder))
    }

    fun findByQuerySqlContaining(q: String?, page: Int?, size: Int?, sortKey: String?, sortOrder: String?): Page<QueryLog>? {
        return queryLogRepo.findByQuerySqlContaining(q, createPageable(page, size, sortKey, sortOrder))
    }

    fun findAllBySelectId(selectId: String, page: Int?, size: Int?, sortKey: String?, sortOrder: String?): Page<QueryLog>? {
        return queryLogRepo.findAllBySelectId(selectId, createPageable(page, size, sortKey, sortOrder))
    }

    fun findAllByAppId(appId: String, page: Int?, size: Int?, sortKey: String?, sortOrder: String?): Page<QueryLog>? {
        return queryLogRepo.findAllByAppId(appId, createPageable(page, size, sortKey, sortOrder))
    }

    private fun createPageable(page: Int?, size: Int?, sortKey: String?, sortOrder: String?): Pageable {
        val page = page ?: 0
        var size = size ?: 10
        val orders: MutableList<Sort.Order> = ArrayList()


        if (size < 0) {
            size = Int.MAX_VALUE
        }

        if (sortKey.isNullOrEmpty() || sortOrder.isNullOrEmpty()) {
            return Pageable.ofSize(size).withPage(page)
        }

        val k: List<String> = sortKey.split(",")
        val o: List<String> = sortOrder.split(",")
        if (k.size != o.size) {
            return Pageable.ofSize(size).withPage(page)
        }

        if (k.isNotEmpty()) {
            for (i in k.indices) {
                if (o[i].equals("asc", ignoreCase = true)) {
                    orders.add(Sort.Order.asc(k[i]))
                } else {
                    orders.add(Sort.Order.desc(k[i]))
                }
            }
        }
        val sortBy = Sort.by(orders)
        if (sortBy.isUnsorted) {
            return Pageable.ofSize(size).withPage(page)
        } else {
            return PageRequest.of(page, size, sortBy)
        }
    }

}
