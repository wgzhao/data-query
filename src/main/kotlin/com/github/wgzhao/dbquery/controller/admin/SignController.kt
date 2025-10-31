package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.ApiResponse
import com.github.wgzhao.dbquery.entities.Sign
import com.github.wgzhao.dbquery.service.SignService
import com.github.wgzhao.dbquery.util.SignUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/signs")
open class SignController(private val signService: SignService) {
    private val logger = KotlinLogging.logger {  }


    @GetMapping
    fun list(): List<Sign>? {
        return signService.findAll()
    }

    @PostMapping
    fun save(@RequestBody sign: Sign): Sign {
        return signService.save(sign)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody sign: Sign): ApiResponse<String?> {
        if (sign.appId != id) {
            return ApiResponse.error(400, "Sign id does not match")
        }
        // check the id is valid
        if (!signService.existsById(id)) {
            return ApiResponse.error(400, "Sign id does not exist")
        }
        signService.save(sign)
        return ApiResponse.success("Sign updated")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResponse<String?> {
        signService.deleteById(id)
        return ApiResponse.success(null)
    }

    @GetMapping("/gen")
    fun genSign(): Sign {
        return SignUtil.generateSign()
    }
}