package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.dto.DbSourceDto;
import com.github.wgzhao.dbquery.entities.DataSources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DataSourceRepo extends JpaRepository<DataSources, String> {
    @Query("select new com.github.wgzhao.dbquery.dto.DbSourceDto(no, name) from DataSources")
    Collection<DbSourceDto> findNoAndName();
}
