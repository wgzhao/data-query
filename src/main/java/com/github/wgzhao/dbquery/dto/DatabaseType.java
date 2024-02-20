package com.github.wgzhao.dbquery.dto;

import lombok.Getter;

@Getter
public enum DatabaseType {
    MYSQL("mysql"),
    POSTGRESQL("postgresql"),
    SQLSERVER("sqlserver"),
    ORACLE("oracle"),
    DB2("db2"),
    H2("h2"),
    HSQLDB("hsqldb"),
    SQLITE("sqlite"),
    MARIADB("mariadb"),
    OTHER("other");

    private final String value;

    DatabaseType(String value) {
        this.value = value;
    }

    public static DatabaseType fromValue(String value) {
        for (DatabaseType type : DatabaseType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return OTHER;
    }
}
