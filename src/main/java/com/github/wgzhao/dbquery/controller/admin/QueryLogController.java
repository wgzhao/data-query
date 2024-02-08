package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.entities.QueryLog;
import com.github.wgzhao.dbquery.repo.QueryLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin/api/v1/querylogs")
public class QueryLogController
{
    @Autowired
    private QueryLogRepo queryLogRepo;

    @GetMapping
    public Page<QueryLog> list(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size)
    {
        return queryLogRepo.findAll(Pageable.ofSize(size).withPage(page));
    }

    @GetMapping("/by/selectId/{selectId}")
    public Page<QueryLog> listBySelectId(
            @PathVariable("selectId") String selectId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size)
    {
        return queryLogRepo.findAllBySelectId(selectId, Pageable.ofSize(size).withPage(page));
    }

    @GetMapping("/by/appId/{appId}")
    public Page<QueryLog> listByAppId(
            @PathVariable("appId") String appId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size)
    {
        return queryLogRepo.findAllByAppId(appId, Pageable.ofSize(size).withPage(page));
    }
}
