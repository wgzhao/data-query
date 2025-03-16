package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import com.github.wgzhao.dbquery.entities.QueryConfigSign;
import com.github.wgzhao.dbquery.entities.Sign;
import com.github.wgzhao.dbquery.repo.QueryConfigRepo;
import com.github.wgzhao.dbquery.repo.QueryConfigSignRepo;
import com.github.wgzhao.dbquery.repo.SignRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.manage-prefix}/query-config-sign")
public class QueryConfigSignController {

    private final QueryConfigSignRepo queryConfigSignRepo;
    private final QueryConfigRepo queryConfigRepo;
    private final SignRepo signRepo;

    @GetMapping
    public List<QueryConfigSign> list() {
        return queryConfigSignRepo.findAll();
    }

    @GetMapping("/query/{SelectId}")
    public List<QueryConfigSign> getByQueryConfig(@PathVariable("SelectId") String SelectId) {
        return queryConfigSignRepo.findBySelectId(SelectId);
    }

    @GetMapping("/sign/{signId}")
    public List<QueryConfigSign> getBySign(@PathVariable("signId") String signId) {
        return queryConfigSignRepo.findBySignId(signId);
    }

    @PostMapping
    public CommResponse save(@RequestBody QueryConfigSign queryConfigSign) {
        // Validate both query config and sign exist
        Optional<QueryConfig> queryConfig = queryConfigRepo.findById(queryConfigSign.getSelectId());
        Optional<Sign> sign = signRepo.findById(queryConfigSign.getSignId());

        if (queryConfig.isEmpty()) {
            return new CommResponse(400, "Query configuration not found", null);
        }

        if (sign.isEmpty()) {
            return new CommResponse(400, "Sign not found", null);
        }

        // Check if the relationship already exists
        if (queryConfigSignRepo.existsBySelectIdAndSignId(
                queryConfigSign.getSelectId(), queryConfigSign.getSignId())) {
            return new CommResponse(400, "This association already exists", null);
        }

        queryConfigSignRepo.save(queryConfigSign);
        return new CommResponse(200, "Association created successfully", null);
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") Long id) {
        if (queryConfigSignRepo.existsById(id)) {
            queryConfigSignRepo.deleteById(id);
            return new CommResponse(200, "Association deleted successfully", null);
        } else {
            return new CommResponse(404, "Association not found", null);
        }
    }

    @DeleteMapping("/query/{SelectId}/sign/{signId}")
    public CommResponse deleteByQueryConfigAndSign(
            @PathVariable("SelectId") String SelectId,
            @PathVariable("signId") String signId) {

        QueryConfigSign association = queryConfigSignRepo.findBySelectIdAndSignId(SelectId, signId);

        if (association != null) {
            queryConfigSignRepo.delete(association);
            return new CommResponse(200, "Association deleted successfully", null);
        } else {
            return new CommResponse(404, "Association not found", null);
        }
    }
}