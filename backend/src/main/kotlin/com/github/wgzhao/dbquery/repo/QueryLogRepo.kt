package com.github.wgzhao.dbquery.repo

import com.github.wgzhao.dbquery.entities.QueryLog
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface QueryLogRepo : JpaRepository<QueryLog, Long> {
    fun findBySelectId(selectId: String?): MutableList<QueryLog?>?

    fun findAllBySelectId(selectId: String?, pageable: Pageable?): Page<QueryLog>?

    fun findAllByAppId(appId: String?, pageable: Pageable?): Page<QueryLog>?

    fun findByQuerySqlContaining(s: String?, pageable: Pageable?): Page<QueryLog>?

    @Query(
        value = """
                select date(createdAt) as d, count(1) as num
                from QueryLog
                where createdAt > ?1
                group by date(createdAt) order by d asc
                """,
        nativeQuery = false
    )
    fun statisticByDate(date: Date?): MutableList<MutableMap<String?, Any?>?>?
}
