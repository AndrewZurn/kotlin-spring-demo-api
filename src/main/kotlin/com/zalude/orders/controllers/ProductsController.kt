package com.zalude.orders.controllers

import com.zalude.orders.models.domain.Product
import com.zalude.orders.models.web.CreateProductRequest
import com.zalude.orders.models.web.ErrorWrapper
import com.zalude.orders.models.web.FieldError
import com.zalude.orders.models.web.ProductResponseWrapper
import com.zalude.orders.services.ProductsService
import com.zalude.orders.validators.ProductRequestValidator
import mu.KLogging
import mu.KotlinLogging
import org.funktionale.either.Either
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime
import java.util.*

/**
 * @author awzurn@gmail.com - 4/15/18.
 */
@RestController
@RequestMapping("/products")
class ProductsController(private val productsService: ProductsService,
                         private val productRequestValidator: ProductRequestValidator) {

  @GetMapping
  suspend fun getProduct(): ResponseEntity<ProductResponseWrapper> =
    ResponseEntity(ProductResponseWrapper(productsService.getAllProducts()), OK)

  @GetMapping("/{id}")
  suspend fun getProduct(@PathVariable("id") id: UUID): ResponseEntity<Product> =
    productsService
      .getProduct(id)
      ?.let { ResponseEntity(it, OK) }
      .getOrElse { ResponseEntity(NOT_FOUND) }

  @PostMapping
  suspend fun createProduct(@RequestBody product: CreateProductRequest, errors: Errors): ResponseEntity<Any> {
    productRequestValidator.validate(product, errors)
    if (errors.hasErrors()) {
      logger.info("Invalid create product request: $product, errors: ${errors.allErrors}")
      return ResponseEntity(
        ErrorWrapper(errors.fieldErrors.map { FieldError(it.field, it.defaultMessage.orEmpty()) }),
        BAD_REQUEST
      )
    } else {
      val result = productsService.saveProduct(Product(UUID.randomUUID(), product.name, product.description, OffsetDateTime.now()))
      return when (result) {
        is Either.Right -> {
          logger.info("Successfully created new product - id: ${result.r.id}")
          ResponseEntity(result.r, CREATED)
        }
        is Either.Left -> {
          logger.warn("Could not create new product: ${result.l}")
          ResponseEntity(result.l, INTERNAL_SERVER_ERROR)
        }
      }
    }
  }

  companion object : KLogging()
}

inline fun <T> T?.getOrElse(f: () -> T): T = this ?: f()

fun <T> T?.isDefined(): Boolean = !this.isEmpty()
fun <T> T?.isEmpty(): Boolean = this == null
