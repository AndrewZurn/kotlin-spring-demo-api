package com.zalude.orders

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.kotlin.experimental.coroutine.EnableCoroutine
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableCoroutine
open class OrdersApiApplication

fun main(args: Array<String>) {
  runApplication<OrdersApiApplication>(*args)
}
