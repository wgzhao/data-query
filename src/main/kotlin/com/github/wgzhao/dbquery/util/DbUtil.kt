package com.github.wgzhao.dbquery.util

import com.github.wgzhao.dbquery.dto.DatabaseType
import com.github.wgzhao.dbquery.entities.DataSources
import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties

object DbUtil {
    private val logger = LoggerFactory.getLogger(DbUtil::class.java)

    // guess database type from jdbc url
    fun getDbType(url: String): DatabaseType = when {
        url.contains("mysql", ignoreCase = true) -> DatabaseType.MYSQL
        url.contains("postgresql", ignoreCase = true) -> DatabaseType.POSTGRESQL
        url.contains("sqlserver", ignoreCase = true) -> DatabaseType.SQLSERVER
        url.contains("oracle", ignoreCase = true) -> DatabaseType.ORACLE
        url.contains("db2", ignoreCase = true) -> DatabaseType.DB2
        url.contains("h2", ignoreCase = true) -> DatabaseType.H2
        url.contains("hsqldb", ignoreCase = true) -> DatabaseType.HSQLDB
        url.contains("sqlite", ignoreCase = true) -> DatabaseType.SQLITE
        url.contains("mariadb", ignoreCase = true) -> DatabaseType.MARIADB
        else -> DatabaseType.OTHER
    }

    // test connection for jdbc url
    fun testConnect(db: DataSources): String {
        try {
            Class.forName(db.driver)
            val urlLower = db.url.lowercase()
            val properties = Properties().apply {
                setProperty("user", db.username)
                db.password?.let { setProperty("password", it) }
                if (!urlLower.contains("trino")) {
                    setProperty("connectTimeout", "5000")
                }
            }

            DriverManager.getConnection(db.url, properties).use { connection ->
                connection.createStatement().use { stmt ->
                    if (urlLower.contains("oracle")) {
                        stmt.execute("SELECT 1 FROM DUAL")
                    } else {
                        stmt.execute("SELECT 1")
                    }
                }
                return "success"
            }
        } catch (e: ClassNotFoundException) {
            logger.error("Driver not found: {}", db.driver, e)
            return "Driver not found: ${db.driver}"
        } catch (e: SQLException) {
            logger.error("Connection failed: {}", e.message, e)
            return e.message ?: e.toString()
        }
    }
}
