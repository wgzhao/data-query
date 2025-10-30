package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "signs")
data class Sign(
    @Id
    var appId: String,
    var appKey: String,
    var applier: String? = null,
    var enabled: Boolean = false
) : BaseEntity() {
    override fun toString(): String {
        return this.appId + this.appKey
    }
}
