package com.github.wgzhao.dbquery.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Entity
@Table(name = "query_logs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
class QueryLog

    : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0

    private var appId: String? = null

    private var selectId: String? = null

    private var querySql: String? = null
}
