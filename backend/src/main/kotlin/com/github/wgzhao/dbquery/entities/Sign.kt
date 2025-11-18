package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Timestamp

@Entity
@Table(name = "signs")
data class Sign(
    @Id
    var appId: String,
    var appKey: String,
    var applier: String? = null,
    var enabled: Boolean = false,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis())
)
