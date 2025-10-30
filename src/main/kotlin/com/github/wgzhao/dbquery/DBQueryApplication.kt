package com.github.wgzhao.dbquery

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
object DBQueryApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(DBQueryApplication::class.java, *args)
    }
}
