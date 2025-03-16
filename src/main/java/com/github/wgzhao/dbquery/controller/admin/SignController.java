package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.entities.Sign;
import com.github.wgzhao.dbquery.repo.QueryConfigSignRepo;
import com.github.wgzhao.dbquery.repo.SignRepo;
import com.github.wgzhao.dbquery.util.SignUtil;
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
@RequiredArgsConstructor
@RequestMapping("${app.api.manage-prefix}/sign")
public class SignController {

    private final SignRepo signRepo;
    private final QueryConfigSignRepo queryConfigSignRepo;

    @GetMapping
    public List<Sign> list() {
        return signRepo.findAll();
    }

    @PostMapping
    public Sign save(@RequestBody Sign sign) {
        return signRepo.save(sign);
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") String id) {
        // Check if this sign is associated with any query configurations
        if (queryConfigSignRepo.existsBySignId(id)) {
            return new CommResponse(400, "Cannot delete: Sign is used by one or more query configurations", null);
        }

        if (signRepo.existsById(id)) {
            signRepo.deleteById(id);
            return new CommResponse(200, "Sign deleted successfully", null);
        } else {
            return new CommResponse(200, "Sign has already been deleted", null);
        }
    }

    @GetMapping("/gen")
    public Sign genSign() {
        return SignUtil.generateSign();
    }
}