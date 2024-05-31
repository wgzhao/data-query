package com.github.wgzhao.dbquery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueryStatDto
{
    private Date d;
    private String type;
    private int num;
}
