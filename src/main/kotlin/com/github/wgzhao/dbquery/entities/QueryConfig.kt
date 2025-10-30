package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Getter
import lombok.Setter

@Entity
@Table(name = "query_config")
data class QueryConfig(
    @Id
    val selectId: String? = null,
    val querySql: String? = null,
    val dataSource: String? = null,
    val cacheTime: Int = 0,
    val note: String? = null,
    val isEnable: Boolean = false,
    val enableCache: Boolean = false
) : BaseEntity() {}
