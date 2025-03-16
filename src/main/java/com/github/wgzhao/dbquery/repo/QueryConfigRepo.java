package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryConfigRepo extends JpaRepository<QueryConfig, String> {
    boolean existsByDataSource(String id);
}
