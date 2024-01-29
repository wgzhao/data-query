package com.github.wgzhao.dbquery.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="signs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Sign
{
    @Id
    private String appId;

    private String appKey;

    private String applier;
}
