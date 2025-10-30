package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "query_params")
data class QueryParam(
    @Id
    var selectId: String? = null,
    var paramName: String? = null,
    var paramType: String? = null,
    var isRequired: Boolean = true
) : BaseEntity()

