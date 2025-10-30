package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "data_sources")
data class DataSources(
    @Id
    val no: String? = null,
    val name: String? = null,
    val url: String,
    val username: String? = null,
    val password: String? = null,
    val driver: String? = null
) : BaseEntity() {
    override fun toString(): String {
        return String.format(
            " url: %s, \t" +
                    " username: %s, \t" +
                    " password: %s, \t", url, username, password
        )
    }
}
