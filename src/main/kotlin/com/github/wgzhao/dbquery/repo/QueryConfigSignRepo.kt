package com.github.wgzhao.dbquery.repo

import com.github.wgzhao.dbquery.entities.QueryConfigSign
import org.springframework.data.jpa.repository.JpaRepository

interface QueryConfigSignRepo : JpaRepository<QueryConfigSign?, Long?> {
    fun existsByAppId(AppId: String?): Boolean

    fun findBySelectId(queryConfigId: String?): MutableList<QueryConfigSign?>?

    fun findByAppId(AppId: String?): MutableList<QueryConfigSign?>?

    fun existsBySelectIdAndAppId(queryConfigId: String?, AppId: String?): Boolean

    fun findBySelectIdAndAppId(queryConfigId: String?, AppId: String?): QueryConfigSign?

    fun deleteByAppId(appId: String?)
}