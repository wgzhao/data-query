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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Sign save(@RequestBody  Sign sign) {
        return signRepo.save(sign);
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(@PathVariable("id") String id) {
        if (signRepo.existsById(id)) {
            signRepo.deleteById(id);
            return new CommResponse(true, "");
        } else {
            return new CommResponse(true, "Sign has deleted");
        }
    }

    @GetMapping("/gen")
    public Sign genSign() {
        return SignUtil.generateSign();
    }
}
