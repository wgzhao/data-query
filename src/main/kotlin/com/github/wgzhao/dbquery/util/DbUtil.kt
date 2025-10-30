package com.github.wgzhao.dbquery.util

import com.github.wgzhao.dbquery.dto.DatabaseType
import com.github.wgzhao.dbquery.entities.DataSources
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

@Slf4j
object DbUtil {
    private val logger = LoggerFactory.getLogger(DbUtil::class.java)

    // guess database type from jdbc url
    fun getDbType(url: String): DatabaseType {
        if (url.contains("mysql")) {
            return DatabaseType.MYSQL
        } else if (url.contains("postgresql")) {
            return DatabaseType.POSTGRESQL
        } else if (url.contains("sqlserver")) {
            return DatabaseType.SQLSERVER
        } else if (url.contains("oracle")) {
            return DatabaseType.ORACLE
        } else if (url.contains("db2")) {
            return DatabaseType.DB2
        } else if (url.contains("h2")) {
            return DatabaseType.H2
        } else if (url.contains("hsqldb")) {
            return DatabaseType.HSQLDB
        } else if (url.contains("sqlite")) {
            return DatabaseType.SQLITE
        } else if (url.contains("mariadb")) {
            return DatabaseType.MARIADB
        } else {
            return DatabaseType.OTHER
        }
    }

    // test connection for jdbc url
    fun testConnect(db: DataSources): String? {
        val result: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        try {
            Class.forName(db.driver)
            val properties = Properties()
            properties.setProperty("user", db.username)
            if (db.password!= null) {
                properties.setProperty("password", db.password)
            }
            // set connect timeout for 5 seconds
            if (!db.url.contains("trino")) {
                properties.setProperty("connectTimeout", "5000")
            }
            val connection = DriverManager.getConnection(db.url, properties)
            if (connection != null) {
                if (db.url.contains("oracle")) {
                    connection.createStatement().execute("SELECT 1 FROM DUAL")
                } else {
                    connection.createStatement().execute("SELECT 1")
                }
                connection.close()
                return "success"
            }
        } catch (e: ClassNotFoundException) {
            logger.error("Driver not found: {}", db.driver)
            return "Driver not found: " + db.driver
        } catch (e: SQLException) {
            logger.error("Connection failed: {}", e.message)
            return e.message
        }
        return "Unknown error"
    }
}
