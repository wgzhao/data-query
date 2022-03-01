package com.lczq.dbquery.constant;

public enum DataBaseType
{
    MySQL("mysql", "com.mysql.cj.jdbc.Driver"),
    Hive("hive", "org.apache.hive.jdbc.HiveDriver"),
    Oracle("oracle", "oracle.jdbc.OracleDriver"),
    Presto("presto", "io.prestosql.jdbc.PrestoDriver"),
    ClickHouse("clickhouse", "com.clickhouse.jdbc.ClickHouseDriver"),
    SQLite("sqlite", "org.sqlite.JDBC"),
    SQLServer("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    PostgreSQL("postgresql", "org.postgresql.Driver"),
    RDBMS("rdbms", "com.wgzhao.addax.rdbms.util.DataBaseType"),
    DB2("db2", "com.ibm.db2.jcc.DB2Driver"),
    Inceptor2("inceptor2", "org.apache.hive.jdbc.HiveDriver"),
    InfluxDB("influxdb", "org.influxdb.influxdb-java"),
    Impala("impala", "com.cloudera.impala.jdbc41.Driver"),
    Trino("trino", "io.trino.jdbc.TrinoDriver");

    private final String typeName;
    private String driverClassName;

    DataBaseType(String typeName, String driverClassName)
    {
        this.typeName = typeName;
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName()
    {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName)
    {
        this.driverClassName = driverClassName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
