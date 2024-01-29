package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.entities.CommResponse;
import com.github.wgzhao.dbquery.entities.Sign;
import com.github.wgzhao.dbquery.repo.SignRepo;
import com.github.wgzhao.dbquery.util.SignUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sign")
@CrossOrigin
public class SignController {

    private final SignRepo signRepo;

    @GetMapping
    public List<Sign> list() {
        return signRepo.findAll();
    }

    @PostMapping
    public Sign save(Sign sign) {
        return signRepo.save(sign);
    }

    @DeleteMapping("/{id}")
    public CommResponse delete(String id) {
        if (signRepo.existsById(id)) {
            signRepo.deleteById(id);
            return new CommResponse(true, "");
        } else {
            return new CommResponse(true, "Signs has deleted");
        }
    }

    @GetMapping("/gen")
    public Sign genSign() {
        return SignUtil.generateSign();
    }
}
