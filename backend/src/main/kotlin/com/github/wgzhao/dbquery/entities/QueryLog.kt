package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Id
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.sql.Timestamp

@Entity
@Table(name = "query_logs")
data class QueryLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var appId: String,
    var selectId: String,
    var querySql: String,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis())
)
