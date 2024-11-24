package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.util.DbUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${app.api.manage-prefix}/dataSource")
@RequiredArgsConstructor
public class DataSourcesController
{

    private final DataSourceRepo dataSourceRepo;

    @GetMapping
    public List<DataSources> list()
    {
        return dataSourceRepo.findAll();
    }

    @GetMapping("/{id}")
    public DataSources get(@PathVariable("id") String id)
    {
        return dataSourceRepo.findById(id).orElse(null);
    }

    @PostMapping("/testConnection")
    public CommResponse testConnection(@RequestBody DataSources db)
    {
        return DbUtil.testConnect(db);
    }

    @PostMapping
    public CommResponse save(@RequestBody DataSources db)
    {
        if (dataSourceRepo.findById(db.getNo()).isPresent()) {
            return new CommResponse(400, "编号(" +db.getNo() + ")已存在", null);
        } else {
            dataSourceRepo.save(db);
            return new CommResponse(201, "success", null);
        }
    }

    @PutMapping
    public CommResponse update(@RequestBody DataSources db)
    {
        if (dataSourceRepo.findById(db.getNo()).isPresent()) {
            dataSourceRepo.save(db);
            return new CommResponse(200, "success", null);
        } else {
            return new CommResponse(400, "此数据源不存在", null);
        }
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") String id)
    {
        if (dataSourceRepo.existsById(id)) {
            dataSourceRepo.deleteById(id);
            return new CommResponse(200, "", null);
        }
        else {
            return new CommResponse(200, "已删除", null);
        }
    }
}
