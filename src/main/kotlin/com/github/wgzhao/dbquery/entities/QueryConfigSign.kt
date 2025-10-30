package com.github.wgzhao.dbquery.entities

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Entity
@Table(name = "query_config_sign")

data class QueryConfigSign (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null,
    private val selectId: String? = null,
    private val appId: String? = null
)