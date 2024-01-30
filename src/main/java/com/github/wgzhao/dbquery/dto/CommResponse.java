package com.github.wgzhao.dbquery.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommResponse {
    private boolean success;
    private String message;
}
