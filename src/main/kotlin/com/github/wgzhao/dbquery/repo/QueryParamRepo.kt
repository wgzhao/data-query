package com.github.wgzhao.dbquery.repo

import com.github.wgzhao.dbquery.entities.QueryParam
import org.springframework.data.jpa.repository.JpaRepository

interface QueryParamRepo : JpaRepository<QueryParam, String> {
    fun findBySelectId(selectId: String?): MutableList<QueryParam?>?
}
