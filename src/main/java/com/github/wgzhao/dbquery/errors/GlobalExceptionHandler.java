package com.github.wgzhao.dbquery.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleMissingRequestParameter(HttpServletRequest request, Exception ex)
    {
        logger.info("Exception occurred: URI= " + request.getRequestURI() + ", error: " + ex.getMessage());
        
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
