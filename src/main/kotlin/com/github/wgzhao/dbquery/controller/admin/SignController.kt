package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.dto.ApiResponse
import com.github.wgzhao.dbquery.entities.QueryConfigSign
import com.github.wgzhao.dbquery.entities.Sign
import com.github.wgzhao.dbquery.repo.QueryConfigSignRepo
import com.github.wgzhao.dbquery.repo.SignRepo
import com.github.wgzhao.dbquery.util.SignUtil
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("\${app.api.manage-prefix}/signs")
@Slf4j
class SignController {
    private val signRepo: SignRepo? = null
    private val queryConfigSignRepo: QueryConfigSignRepo? = null

    @PersistenceContext
    private val entityManager: EntityManager? = null

    @GetMapping
    fun list(): MutableList<Sign?> {
        return signRepo!!.findAll()
    }

    @PostMapping
    fun save(@RequestBody sign: Sign): Sign {
        return signRepo!!.save<Sign>(sign)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody sign: Sign): ApiResponse<String?> {
        if (sign.getAppId() != id) {
            return ApiResponse.Companion.error<String?>(400, "Sign id does not match")
        }
        // check the id is valid
        if (!signRepo!!.existsById(id)) {
            return ApiResponse.Companion.error<String?>(400, "Sign id does not exist")
        }
        signRepo.save<Sign?>(sign)
        return ApiResponse.Companion.success<String?>("Sign updated")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResponse<String?> {
        // Check if this sign is associated with any query configurations
        if (queryConfigSignRepo!!.existsByAppId(id)) {
            return ApiResponse.Companion.error<String?>(
                400,
                "Cannot delete: Sign is used by one or more query configurations"
            )
        }

        if (signRepo!!.existsById(id)) {
            signRepo.deleteById(id)
        }
        return ApiResponse.Companion.success<String?>(null)
    }

    @GetMapping("/gen")
    fun genSign(): Sign {
        return SignUtil.generateSign()
    }

    @GetMapping("/query-configs")
    fun queryConfigSigns(): MutableList<QueryConfigSign?> {
        return queryConfigSignRepo!!.findAll()
    }

    @GetMapping("/query-configs/{appId}")
    fun queryConfigSignsBySign(@PathVariable("appId") appId: String?): MutableList<QueryConfigSign?>? {
        return queryConfigSignRepo!!.findByAppId(appId)
    }

    @PostMapping("/query-configs/{appId}")
    @Transactional
    fun saveQueryConfigSign(
        @PathVariable("appId") appId: String?,
        @RequestBody queryConfigIds: MutableList<String?>
    ): ApiResponse<String?> {
        SignController.log.info("Associating sign {} with query configurations: {}", appId, queryConfigIds)
        // delete all associations first
        queryConfigSignRepo!!.deleteByAppId(appId)
        entityManager!!.flush() // Ensure the delete operation is immediately executed

        for (queryConfigId in queryConfigIds) {
            val queryConfigSign = QueryConfigSign()
            queryConfigSign.setSelectId(queryConfigId)
            queryConfigSign.setAppId(appId)
            queryConfigSignRepo.save<QueryConfigSign?>(queryConfigSign)
        }
        return ApiResponse.Companion.success<String?>(null)
    }
}