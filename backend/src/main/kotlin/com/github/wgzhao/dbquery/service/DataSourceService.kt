package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.dto.DbSourceDto
import com.github.wgzhao.dbquery.entities.DataSources
import com.github.wgzhao.dbquery.repo.DataSourceRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DataSourceService(
    val dataSourceRepo: DataSourceRepo
) {

    fun findAll() = dataSourceRepo.findAll()

    fun findNoAndName(): List<DbSourceDto>
    {
        return dataSourceRepo.findAll().map {
            DbSourceDto(
                no = it.no,
                name = it.name
            )
        }
    }

    fun findById(id: String) = dataSourceRepo.findById(id)

    fun deleteById(id: String) = dataSourceRepo.deleteById(id)

    @Transactional
    fun save(db: DataSources) = dataSourceRepo.save(db)
}