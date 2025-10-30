package com.github.wgzhao.dbquery.entities

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import lombok.Getter
import lombok.Setter
import java.io.Serial
import java.io.Serializable
import java.util.*

@MappedSuperclass
@Getter
@Setter
open class BaseEntity : Serializable {
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private var createdAt: Date? = null

    @Column(
        name = "updated_at",
        nullable = false,
        insertable = false,
        updatable = false
    ) //    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private var updatedAt: Date? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
