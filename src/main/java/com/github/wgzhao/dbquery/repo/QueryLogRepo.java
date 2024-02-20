package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryLogRepo
        extends JpaRepository<QueryLog, Long> {
    List<QueryLog> findBySelectId(String selectId);

    Page<QueryLog> findAllBySelectId(String selectId, Pageable pageable);

    Page<QueryLog> findAllByAppId(String appId, Pageable pageable);
}
