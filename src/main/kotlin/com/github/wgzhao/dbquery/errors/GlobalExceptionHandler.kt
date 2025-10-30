package com.github.wgzhao.dbquery.errors

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingRequestParameter(request: HttpServletRequest, ex: Exception): ResponseEntity<String?> {
        logger.info("Exception occurred: URI= " + request.getRequestURI() + ", error: " + ex.message)

        return ResponseEntity.status(400).body<String?>(ex.message)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}
