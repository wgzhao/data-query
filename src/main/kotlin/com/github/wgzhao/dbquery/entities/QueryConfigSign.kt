package com.github.wgzhao.dbquery.entities

import jakarta.persistence.*

@Entity
@Table(name = "query_config_sign")

data class QueryConfigSign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var selectId: String? = null,
    var appId: String? = null
)