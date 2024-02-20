package com.github.wgzhao.dbquery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationRequest {

    private String username;
    private String password;
}
