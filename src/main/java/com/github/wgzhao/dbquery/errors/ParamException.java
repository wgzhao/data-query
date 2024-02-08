package com.github.wgzhao.dbquery.errors;

import java.io.Serial;

public class ParamException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public ParamException(String message) {
        super(message);
    }
}
