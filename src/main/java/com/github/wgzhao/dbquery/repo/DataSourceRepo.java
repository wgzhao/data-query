package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.DataSources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourceRepo extends JpaRepository<DataSources, String> {
}
