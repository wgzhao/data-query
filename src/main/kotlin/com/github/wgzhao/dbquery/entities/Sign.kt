package com.github.wgzhao.dbquery.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Entity
@Table(name = "signs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
class Sign(
    @Id
    val appId: String? = null,
    val appKey: String? = null,
    val applier: String? = null,
    val enabled: Boolean = false
) {
    override fun toString(): String {
        return this.appId + this.appKey
    }
}
