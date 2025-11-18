package com.github.wgzhao.dbquery.repo

import com.github.wgzhao.dbquery.entities.QueryConfigSign
import org.springframework.data.jpa.repository.JpaRepository

interface QueryConfigSignRepo : JpaRepository<QueryConfigSign, Long> {
    fun existsByAppId(appId: String): Boolean

    fun findBySelectId(queryConfigId: String?): MutableList<QueryConfigSign?>?

    fun findByAppId(appId: String): List<QueryConfigSign?>?

    fun existsBySelectIdAndAppId(queryConfigId: String?, appId: String?): Boolean

    fun findBySelectIdAndAppId(queryConfigId: String?, appId: String?): QueryConfigSign?

    fun deleteByAppId(appId: String?)
}