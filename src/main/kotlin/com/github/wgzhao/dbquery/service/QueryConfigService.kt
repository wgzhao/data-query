package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.entities.QueryConfig
import com.github.wgzhao.dbquery.repo.QueryConfigRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueryConfigService(private val queryConfigRepo: QueryConfigRepo) {
    fun getAll() = queryConfigRepo.findAll()

    fun findById(id: String) = queryConfigRepo.findById(id)

    @Transactional
    fun deleteById(id: String) = queryConfigRepo.deleteById(id)

    @Transactional
    fun save(entity: QueryConfig) = queryConfigRepo.save(entity)

    fun existsByDataSource(dataSourceId: String) = queryConfigRepo.findByDataSource(dataSourceId) != null
}