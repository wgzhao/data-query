package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryConfigSign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryConfigSignRepo extends JpaRepository<QueryConfigSign, Long> {
    boolean existsBySignId(String signId);

    List<QueryConfigSign> findBySelectId(String queryConfigId);

    List<QueryConfigSign> findBySignId(String signId);

    boolean existsBySelectIdAndSignId(String queryConfigId, String signId);

    QueryConfigSign findBySelectIdAndSignId(String queryConfigId, String signId);
}