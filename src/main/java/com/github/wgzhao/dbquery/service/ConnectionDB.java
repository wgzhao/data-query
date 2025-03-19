package com.github.wgzhao.dbquery.service;

import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConnectionDB {

    public List<Map<String, Object>> executeSQL(QueryConfig queryConfig, DataSources dataSource)
            throws ClassNotFoundException, SQLException {

        return executeSQL(queryConfig.getQuerySql(), dataSource);
    }

    public List<Map<String, Object>> executeSQL(String querySql, DataSources dataSource)
            throws ClassNotFoundException, SQLException {
        String dbUrl = dataSource.getUrl();
        String user = dataSource.getUsername();
        String pass = dataSource.getPassword();
        String jdbcDriver = dataSource.getDriver();

        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> rsMap;
        Connection conn;
        Statement stmt;

        Class.forName(jdbcDriver);
        conn = DriverManager.getConnection(dbUrl, user, pass);
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(querySql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        while (rs.next()) {
            rsMap = new HashMap<>(numberOfColumns);
            for (int i = 1; i < numberOfColumns + 1; i++) {
                rsMap.put(rsmd.getColumnLabel(i), rs.getObject(i));
            }
            result.add(rsMap);
        }
        rs.close();
        stmt.close();
        conn.close();

        return result;
    }
}
