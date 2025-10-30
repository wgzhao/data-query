package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.entities.DataSources
import com.github.wgzhao.dbquery.entities.QueryConfig
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

@Service
class ConnectionDB {
    @Throws(ClassNotFoundException::class, SQLException::class)
    fun executeSQL(queryConfig: QueryConfig, dataSource: DataSources): MutableList<MutableMap<String?, Any?>?> {
        return executeSQL(queryConfig.getQuerySql(), dataSource)
    }

    @Throws(ClassNotFoundException::class, SQLException::class)
    fun executeSQL(querySql: String?, dataSource: DataSources): MutableList<MutableMap<String?, Any?>?> {
        val dbUrl = dataSource.getUrl()
        val user = dataSource.getUsername()
        val pass = dataSource.getPassword()
        val jdbcDriver = dataSource.getDriver()

        val result: MutableList<MutableMap<String?, Any?>?> = ArrayList<MutableMap<String?, Any?>?>()

        var rsMap: MutableMap<String?, Any?>?
        val conn: Connection
        val stmt: Statement

        Class.forName(jdbcDriver)
        conn = DriverManager.getConnection(dbUrl, user, pass)
        stmt = conn.createStatement()
        val rs = stmt.executeQuery(querySql)
        val rsmd = rs.getMetaData()
        val numberOfColumns = rsmd.getColumnCount()
        while (rs.next()) {
            rsMap = HashMap<String?, Any?>(numberOfColumns)
            for (i in 1..<numberOfColumns + 1) {
                rsMap.put(rsmd.getColumnLabel(i), rs.getObject(i))
            }
            result.add(rsMap)
        }
        rs.close()
        stmt.close()
        conn.close()

        return result
    }
}
