package com.github.wgzhao.dbquery.repo

import com.github.wgzhao.dbquery.entities.QueryConfig
import org.springframework.data.jpa.repository.JpaRepository

interface QueryConfigRepo : JpaRepository<QueryConfig, String> {
    fun findByDataSource(dataSource: String): QueryConfig?
}
