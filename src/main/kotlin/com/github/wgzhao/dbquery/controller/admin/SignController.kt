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
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/signs")
open class SignController {
    private val logger = LoggerFactory.getLogger(SignController::class.java)

    @Autowired
    private val signRepo: SignRepo? = null
    @Autowired
    private val queryConfigSignRepo: QueryConfigSignRepo? = null

    @PersistenceContext
    private val entityManager: EntityManager? = null

    @GetMapping
    fun list(): MutableList<Sign?> {
        return signRepo!!.findAll()
    }

    @PostMapping
    fun save(@RequestBody sign: Sign): Sign {
        return signRepo!!.save(sign)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody sign: Sign): ApiResponse<String?> {
        if (sign.appId != id) {
            return ApiResponse.error(400, "Sign id does not match")
        }
        // check the id is valid
        if (!signRepo!!.existsById(id)) {
            return ApiResponse.error(400, "Sign id does not exist")
        }
        signRepo.save(sign)
        return ApiResponse.success("Sign updated")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResponse<String?> {
        // Check if this sign is associated with any query configurations
        if (queryConfigSignRepo!!.existsByAppId(id)) {
            return ApiResponse.error(
                400,
                "Cannot delete: Sign is used by one or more query configurations"
            )
        }

        if (signRepo!!.existsById(id)) {
            signRepo.deleteById(id)
        }
        return ApiResponse.success(null)
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
    open fun saveQueryConfigSign(
        @PathVariable("appId") appId: String?,
        @RequestBody queryConfigIds: MutableList<String?>
    ): ApiResponse<String?> {
        logger.info("Associating sign {} with query configurations: {}", appId, queryConfigIds)
        // delete all associations first
        queryConfigSignRepo!!.deleteByAppId(appId)
        entityManager!!.flush() // Ensure the delete operation is immediately executed

        for (queryConfigId in queryConfigIds) {
            val queryConfigSign = QueryConfigSign(selectId = queryConfigId, appId = appId)
            queryConfigSignRepo.save(queryConfigSign)
        }
        return ApiResponse.success(null)
    }
}