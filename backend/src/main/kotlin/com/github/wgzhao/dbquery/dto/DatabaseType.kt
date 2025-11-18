package com.github.wgzhao.dbquery.dto

enum class DatabaseType(private val value: String) {
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

    companion object {
        fun fromValue(value: String?): DatabaseType {
            for (type in DatabaseType.entries) {
                if (type.value.equals(value, ignoreCase = true)) {
                    return type
                }
            }
            return DatabaseType.OTHER
        }
    }
}
