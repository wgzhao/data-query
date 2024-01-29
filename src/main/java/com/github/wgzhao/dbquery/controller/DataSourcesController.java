package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.entities.CommResponse;
import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.util.DbUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/datasources")
@RequiredArgsConstructor
@CrossOrigin
public class DataSourcesController {

    private final DataSourceRepo dataSourceRepo;

    @GetMapping
    public List<DataSources> list() {
        return dataSourceRepo.findAll();
    }

    @GetMapping("/{id}")
    public DataSources get(@PathVariable("id") String id) {
        return dataSourceRepo.findById(id).orElse(null);
    }

    @PostMapping("/testConnection")
    public CommResponse testConnection(@RequestBody DataSources db) {
        return DbUtil.testConnect(db);
    }

    @PostMapping
    public DataSources save(@RequestBody DataSources db) {
        return dataSourceRepo.save(db);
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") String id) {
        if (dataSourceRepo.existsById(id)) {
            dataSourceRepo.deleteById(id);
            return new CommResponse(true, "");
        } else {
            return new CommResponse(true, "Data source has deleted");
        }
    }
}
