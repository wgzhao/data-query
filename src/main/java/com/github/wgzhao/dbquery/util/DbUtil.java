package com.github.wgzhao.dbquery.util;

import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.dto.DatabaseType;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class DbUtil
{

    // guess database type from jdbc url
    public static DatabaseType getDbType(String url)
    {
        if (url.contains("mysql")) {
            return DatabaseType.MYSQL;
        }
        else if (url.contains("postgresql")) {
            return DatabaseType.POSTGRESQL;
        }
        else if (url.contains("sqlserver")) {
            return DatabaseType.SQLSERVER;
        }
        else if (url.contains("oracle")) {
            return DatabaseType.ORACLE;
        }
        else if (url.contains("db2")) {
            return DatabaseType.DB2;
        }
        else if (url.contains("h2")) {
            return DatabaseType.H2;
        }
        else if (url.contains("hsqldb")) {
            return DatabaseType.HSQLDB;
        }
        else if (url.contains("sqlite")) {
            return DatabaseType.SQLITE;
        }
        else if (url.contains("mariadb")) {
            return DatabaseType.MARIADB;
        }
        else {
            return DatabaseType.OTHER;
        }
    }

    // test connection for jdbc url
    public static CommResponse testConnect(DataSources db)
    {
        Map<String, Object> result = new HashMap<>();
        try {
            Class.forName(db.getDriver());
            Properties properties = new Properties();
            properties.setProperty("user", db.getUsername());
            if (db.getPassword() != null) {
                properties.setProperty("password", db.getPassword());
            }
            // set connect timeout for 5 seconds
            if (! db.getUrl().contains("trino")) {
                properties.setProperty("connectTimeout", "5000");
            }
            Connection connection = DriverManager.getConnection(db.getUrl(), properties);
            if (connection != null) {
                if (db.getUrl().contains("oracle")) {
                    connection.createStatement().execute("SELECT 1 FROM DUAL");
                }
                else {
                    connection.createStatement().execute("SELECT 1");
                }
                connection.close();
                return new CommResponse(true, "");
            }
        }
        catch (ClassNotFoundException e) {
            log.error("Driver not found: {}", db.getDriver());
            return new CommResponse(false, "Driver not found: " + db.getDriver());
        }
        catch (SQLException e) {
            log.error("Connection failed: {}", e.getMessage());
            return new CommResponse(false, e.getMessage());
        }
        return new CommResponse(false, "Unknown error");
    }
}
