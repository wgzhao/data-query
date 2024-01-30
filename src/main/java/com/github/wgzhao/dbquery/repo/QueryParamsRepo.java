package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryParamsRepo extends JpaRepository<QueryParam, String> {
    List<QueryParam> findBySelectId(String selectId);
}
