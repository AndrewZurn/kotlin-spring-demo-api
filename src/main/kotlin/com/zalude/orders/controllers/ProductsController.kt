package com.zalude.orders.controllers

import com.zalude.orders.models.Product
import com.zalude.orders.services.ProductsService
import org.funktionale.either.Either
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author awzurn@gmail.com - 4/15/18.
 */
@RestController
class ProductsController(private val productsService: ProductsService) {

  @GetMapping("/products")
  suspend fun getProducts(): ResponseEntity<List<Product>> {
    return ResponseEntity(
        productsService.getAllProducts(),
        HttpStatus.OK
    )
  }

  @GetMapping("/products/{id}")
  suspend fun getProducts(@PathVariable("id") id: UUID): ResponseEntity<Product> {
    return productsService
        .getProduct(id)
        .map { ResponseEntity(it, HttpStatus.OK) }
        .getOrElse { ResponseEntity(HttpStatus.NOT_FOUND) }
  }

  @PostMapping("/products")
  suspend fun createProduct(@RequestBody product: ProductRequest): ResponseEntity<Any> {
    val result= productsService.saveProduct(Product(UUID.randomUUID(), product.name, product.description))
    return when(result) {
      is Either.Right -> ResponseEntity(result.right().get(), HttpStatus.CREATED)
      is Either.Left -> ResponseEntity(result.left().get(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }

  data class ProductRequest(val name: String, val description: String)
}

inline fun <T, U> T?.map(f: (T) -> U): U? = if (this != null) f(this) else null

inline fun <T> T?.getOrElse(f: () -> T): T = this ?: f()