package com.lczq.dbquery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lczq.dbquery.mapper")
@SpringBootApplication
public class DBQueryApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(DBQueryApplication.class, args);
    }
}
