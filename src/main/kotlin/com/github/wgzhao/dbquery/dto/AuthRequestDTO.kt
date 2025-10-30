package com.github.wgzhao.dbquery.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
data class AuthRequestDTO(val username: String? = null, val password: String? = null)
