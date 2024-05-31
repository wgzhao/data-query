package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.dto.QueryStatDto;
import com.github.wgzhao.dbquery.entities.QueryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface QueryLogRepo
        extends JpaRepository<QueryLog, Long> {
    List<QueryLog> findBySelectId(String selectId);

    Page<QueryLog> findAllBySelectId(String selectId, Pageable pageable);

    Page<QueryLog> findAllByAppId(String appId, Pageable pageable);

    Page<QueryLog> findByQuerySqlContaining(String s, Pageable pageable);

    @Query(value = """
                select date(createdAt) as d, count(1) as num
                from QueryLog
                where createdAt > ?1
                group by date(createdAt) order by d asc""",
            nativeQuery = false)
    List<Map<String, Object>> statisticByDate(Date date);
}
