package com.github.wgzhao.dbquery.errors

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {}
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingRequestParameter(request: HttpServletRequest, ex: Exception): ResponseEntity<Any> {
        logger.info { "Exception occurred: URI= ${request.requestURI}, error:  ${ex.message}" }

        return ResponseEntity.status(400).body(ex.message)
    }

}
