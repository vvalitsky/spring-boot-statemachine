package com.vvalitsky.stm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StmApplication

fun main(args: Array<String>) {
    runApplication<StmApplication>(*args)
}
