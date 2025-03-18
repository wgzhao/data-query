package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryConfigSign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryConfigSignRepo extends JpaRepository<QueryConfigSign, Long> {
    boolean existsByAppId(String AppId);

    List<QueryConfigSign> findBySelectId(String queryConfigId);

    List<QueryConfigSign> findByAppId(String AppId);

    boolean existsBySelectIdAndAppId(String queryConfigId, String AppId);

    QueryConfigSign findBySelectIdAndAppId(String queryConfigId, String AppId);

    void deleteByAppId(String appId);
}