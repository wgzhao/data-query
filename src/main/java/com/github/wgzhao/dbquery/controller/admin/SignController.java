package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.entities.Sign;
import com.github.wgzhao.dbquery.repo.SignRepo;
import com.github.wgzhao.dbquery.util.SignUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.manage-prefix}/sign")
public class SignController {

    private final SignRepo signRepo;

    @GetMapping
    public List<Sign> list() {
        return signRepo.findAll();
    }

    @PostMapping
    public CommResponse save(@RequestBody  Sign sign) {
        if (signRepo.existsById(sign.getAppId())) {
            return new CommResponse(400, "appId has exists", null);
        }
        signRepo.save(sign);
        return new CommResponse(201, "success", null);
    }

    @PutMapping
    public CommResponse update(@RequestBody Sign sign) {
        if (!signRepo.existsById(sign.getAppId())) {
            return new CommResponse(400, "appId does not exists", null);
        } else {
            signRepo.save(sign);
            return new CommResponse(200, "sign have updated", null);
        }
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") String id) {
        if (signRepo.existsById(id)) {
            signRepo.deleteById(id);
            return new CommResponse(200, "success", null);
        } else {
            return new CommResponse(200, "Sign has deleted", null);
        }
    }

    @GetMapping("/gen")
    public Sign genSign() {
        return SignUtil.generateSign();
    }
}
