package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.QueryLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryLogsRepo extends JpaRepository<QueryLogs, Long> {
}
