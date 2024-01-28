package com.github.wgzhao.dbquery.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
public class QueryConfig extends BaseEntity
{
    @Id
    private String selectId;

    private String querySql;

    private String dataSource;

    private int cacheTime;

    private String note;

    private boolean isEnable;

    private boolean enableCache;
}
