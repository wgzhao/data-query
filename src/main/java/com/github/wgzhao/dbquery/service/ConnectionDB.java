package com.github.wgzhao.dbquery.service;

import com.github.wgzhao.dbquery.entities.DataSources;
import com.github.wgzhao.dbquery.dto.QueryResult;
import com.github.wgzhao.dbquery.entities.QueryConfig;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConnectionDB
{

    public QueryResult executeSQL(QueryConfig queryConfig, DataSources dataSource)
            throws ClassNotFoundException, SQLException
    {
        String dbUrl = dataSource.getUrl();
        String user = dataSource.getUsername();
        String pass = dataSource.getPassword();
        String jdbcDriver = dataSource.getDriver();
        String sql = queryConfig.getQuerySql();

        QueryResult rsList = new QueryResult();
        Map<String, Object> rsMap;
        Connection conn;
        Statement stmt;

        Class.forName(jdbcDriver);
        conn = DriverManager.getConnection(dbUrl, user, pass);

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        while (rs.next()) {
            rsMap = new HashMap<>(numberOfColumns);
            for (int i = 1; i < numberOfColumns + 1; i++) {
                rsMap.put(rsmd.getColumnName(i), rs.getObject(i));
            }
            rsList.setOneMap(rsMap);
        }
        rs.close();
        stmt.close();
        conn.close();

        return rsList;
    }
}
