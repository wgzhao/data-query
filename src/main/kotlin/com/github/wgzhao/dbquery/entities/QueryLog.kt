package com.github.wgzhao.dbquery.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Entity
@Table(name = "query_logs")
data class QueryLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var appId: String,
    var selectId: String,
    var querySql: String
) : BaseEntity()
