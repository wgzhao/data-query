package com.github.wgzhao.dbquery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class QueryResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 691617930352197222L;

    private final String apiVersion = "1.0";
    private int status;
    private boolean success;
    private String message;
    private int total;
    private Map<String, Object> data;

    public QueryResult() {
        this.status = 400;
        this.success = false;
        this.data = Map.of("result", new ArrayList<>());
    }

    public QueryResult(String message) {
        this.status = 400;
        this.success = false;
        this.message = message;
        this.data = Map.of("result", new ArrayList<>());
    }
}