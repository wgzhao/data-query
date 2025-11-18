package com.github.wgzhao.dbquery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DBQueryApplication

fun main(args: Array<String>) {
    runApplication<DBQueryApplication>(*args)
}