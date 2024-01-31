package com.github.wgzhao.dbquery.entities;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "query_params")
@Setter
@Getter
@Data
@IdClass(QueryParam.Pk.class)
public class QueryParam
{
    @Id
    private String selectId;

    @Id
    private String paramName;

    @Id
    private String paramType;

    @Setter
    @Getter
    @Embeddable
    public static class Pk {
        private String selectId;
        private String paramName;
        private String paramType;

    }
}
