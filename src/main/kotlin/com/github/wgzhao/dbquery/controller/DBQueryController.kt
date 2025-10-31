package com.github.wgzhao.dbquery.controller

import com.github.wgzhao.dbquery.constant.Constants
import com.github.wgzhao.dbquery.dto.QueryResult
import com.github.wgzhao.dbquery.service.DBQueryService
import com.github.wgzhao.dbquery.service.SignService
import com.github.wgzhao.dbquery.util.ParamUtil
import com.github.wgzhao.dbquery.util.SignUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*
import java.sql.SQLException

@RestController
@RequestMapping(value = ["\${app.api.query-prefix}"])
@Tag(name = "DBQuery", description = "DB Query APIs")
class DBQueryController(
    val queryService: DBQueryService,
    val signService: SignService,
    val environment: Environment
) {
    private val logger = KotlinLogging.logger {  }

    @GetMapping("/")
    fun index(): String {
        return "Hello, World!"
    }

    @GetMapping(value = ["/query"], produces = ["application/json;charset=UTF-8"])
    fun getQuery(
        @RequestParam selectId: String?,
        @RequestParam allParams: MutableMap<String, String>,
        response: HttpServletResponse
    ): QueryResult {
        return executeQuery(selectId, allParams, response)
    }

    @PostMapping("/query")
    fun postQuery(@RequestBody payload: MutableMap<String, String>, response: HttpServletResponse): QueryResult {
        val selectId = payload.getOrDefault("selectId", "")
        if (selectId.isEmpty()) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return QueryResult.error(HttpServletResponse.SC_BAD_REQUEST, "缺少必要的参数 selectId")
        }
        return executeQuery(selectId, payload, response)
    }

    private fun executeQuery(
        selectId: String?,
        allParams: MutableMap<String, String>,
        response: HttpServletResponse
    ): QueryResult {
        logger.info("Begin to query with selectId {} and all params {}", selectId, allParams)

        //take the all params break into 2 parts, one is the query params, the other is the control params
        val queryParams = ParamUtil.getQueryParams(allParams)
        val controlParams = ParamUtil.getControlParams(allParams)

        try {
            checkControlParams(queryParams, controlParams)
        } catch (e: Exception) {
            logger.error {"Control params is invalid, error msg is ${e.message}" }
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return QueryResult.error(HttpServletResponse.SC_BAD_REQUEST, e.message)
        }

        // convert all query params key to lower case
        val lowerQueryParams = ParamUtil.lowercaseParams(queryParams)
        try {
            // ensure selectId and appId non-null before calling service
            val sid = selectId ?: return QueryResult.error(HttpServletResponse.SC_BAD_REQUEST, "缺少必要的参数 selectId")
            val appId = controlParams[Constants.APP_ID]
                ?: return QueryResult.error(HttpServletResponse.SC_BAD_REQUEST, "缺少必要的参数 ${Constants.APP_ID}")

            // convert lowerQueryParams (MutableMap<String, String>) to MutableMap<String, String> expected by service
            val lowerQueryParamsGeneric: MutableMap<String, String> = HashMap()
            lowerQueryParams.forEach { (k, v) -> lowerQueryParamsGeneric[k] = v }

            val result = queryService.query(sid, appId, lowerQueryParamsGeneric.ifEmpty { null })
            // queryService may return nullable list with nullable maps; normalize to List<Map<String, Any>>?
            val normalizedResult: List<Map<String, Any>>? = result?.map { it }
            val queryResult = QueryResult.success(normalizedResult)
            return queryResult
        } catch (e: ClassNotFoundException) {
            logger.error {"Query failed, error msg is ${e.message}" }
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            return QueryResult.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.message)
        } catch (e: SQLException) {
            logger.error {"Query failed, error msg is ${e.message}" }
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            return QueryResult.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.message)
        } catch (e: RuntimeException) {
            logger.error {"Query failed, error msg is ${e.message}" }
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            return QueryResult.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.message)
        }
    }

    /**
     * check the control params is valid or not
     */
    private fun checkControlParams(
        queryParams: MutableMap<String, String>?,
        controlParams: Map<String?, String?>
    ) {
        // check _sign has exists

        val sign = controlParams.getOrDefault(Constants.SIGN, null)
        Assert.hasLength(sign, "签名不存在")

        if (Constants.MAGIC_SIGN == sign) {
            // it's magic ,skip all validation
            val activeProfiles = environment.activeProfiles
            require(activeProfiles.isNotEmpty() && (activeProfiles[0] == "test" || activeProfiles[0] == "dev")) { "非法签名" }
        } else {
            // check _appId is existing or not
            val appId = controlParams.getOrDefault(Constants.APP_ID, null)
            Assert.hasLength(appId) { "缺少必要的参数 " + Constants.APP_ID }

            val signs = signService.querySign(appId!!)

            require(signs != null) { "无效的 " + Constants.APP_ID }

            require(SignUtil.validSign(sign, queryParams, signs)) { "无效签名 " }
        }
    }
}
