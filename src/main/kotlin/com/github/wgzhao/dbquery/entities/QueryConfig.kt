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
    var selectId: String,
    var querySql: String,
    var dataSource: String,
    var cacheTime: Int = 0,
    var note: String? = null,
    var isEnable: Boolean = false,
    var enableCache: Boolean = false
) : BaseEntity() {}
