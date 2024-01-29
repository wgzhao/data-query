package com.github.wgzhao.dbquery.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="data_sources")
@Setter
@Getter
public class DataSources extends BaseEntity
{

    @Id
    private String no;

    private String name;

    private String url;

    private String username;

    private String password;

    private String driver;

    @Override
    public String toString() {
        return String.format(" url: %s, \t" +
                " username: %s, \t" +
                " password: %s, \t",url, username, password);
    }
}
