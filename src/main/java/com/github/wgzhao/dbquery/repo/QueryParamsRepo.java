package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryParams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryParamsRepo extends JpaRepository<QueryParams, String> {
    List<QueryParams> findBySelectId(String selectId);
}
