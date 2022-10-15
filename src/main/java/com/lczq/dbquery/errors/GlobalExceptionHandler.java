package com.lczq.dbquery.errors;

import com.lczq.dbquery.param.RestResponse;
import com.lczq.dbquery.param.RestResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public RestResponse<String> handleMissingRequestParameter(HttpServletRequest request, Exception ex)
    {
        logger.info("Exception occurred: URI= " + request.getRequestURI() + ", error: " + ex.getMessage());
        return RestResponseBuilder.fail(400, ex.getMessage());
    }
}
