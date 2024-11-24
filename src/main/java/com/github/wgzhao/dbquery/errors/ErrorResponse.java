package com.github.wgzhao.dbquery.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse
{
    private int status;
    private String message;
    private long timestamp;
}
