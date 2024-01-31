package com.github.wgzhao.dbquery.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
