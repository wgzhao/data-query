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
        return executeSQL(queryConfig.querySql, dataSource)
    }

    @Throws(ClassNotFoundException::class, SQLException::class)
    fun executeSQL(querySql: String?, dataSource: DataSources): MutableList<MutableMap<String?, Any?>?> {
        val dbUrl = dataSource.url
        val user = dataSource.username
        val pass = dataSource.password
        val jdbcDriver = dataSource.driver

        val result: MutableList<MutableMap<String?, Any?>?> = ArrayList()

        Class.forName(jdbcDriver)
        DriverManager.getConnection(dbUrl, user, pass).use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(querySql)
                val rsmd = rs.metaData
                val numberOfColumns = rsmd.columnCount
                while (rs.next()) {
                    val rsMap: MutableMap<String?, Any?> = HashMap(numberOfColumns)
                    for (i in 1..numberOfColumns) {
                        rsMap[rsmd.getColumnLabel(i)] = rs.getObject(i)
                    }
                    result.add(rsMap)
                }
                rs.close()
            }
            conn.close()
        }

        return result
    }
}
