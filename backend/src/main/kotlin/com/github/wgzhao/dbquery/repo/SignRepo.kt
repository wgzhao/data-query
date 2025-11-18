package com.github.wgzhao.dbquery.repo

import com.github.wgzhao.dbquery.entities.Sign
import org.springframework.data.jpa.repository.JpaRepository

interface SignRepo : JpaRepository<Sign, String>
