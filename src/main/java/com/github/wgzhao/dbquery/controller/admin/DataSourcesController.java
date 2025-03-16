package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.repo.DataSourceRepo;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import com.github.wgzhao.dbquery.util.DbUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    private final QueryConfigRepo queryConfigRepo;  // Add this


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
    public DataSources save(@RequestBody DataSources db)
    {
        return dataSourceRepo.save(db);
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") String id)
    {
        // check if it has some query config associated with this data sources
        if (queryConfigRepo.existsByDataSource(id)) {
            return new CommResponse(400, "Cannot delete: Data source is used by one or more query configurations", null);
        }
        if (dataSourceRepo.existsById(id)) {
            dataSourceRepo.deleteById(id);
            return new CommResponse(200, "", null);
        }
        else {
            return new CommResponse(200, "Data source has deleted", null);
        }
    }
}
