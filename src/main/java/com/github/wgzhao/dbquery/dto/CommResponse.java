package com.github.wgzhao.dbquery.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommResponse {
    private int code;
    private String message;
    private Map result;
}
