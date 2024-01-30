package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.entities.QueryConfig;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/queryconfig")
public class QueryConfigController {

    @Autowired
    private QueryConfigRepo queryConfigRepo;
    @GetMapping
    public List<QueryConfig> list() {
        return queryConfigRepo.findAll();
    }
}
