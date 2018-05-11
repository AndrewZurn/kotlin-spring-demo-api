package com.zalude.orders.services

import com.zalude.orders.models.domain.Product
import org.funktionale.either.Either
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*

/**
 * @author awzurn@gmail.com - 4/15/18.
 */
@Component
class ProductsService {

  private val products: MutableList<Product> = mutableListOf<Product>(
    Product(UUID.fromString("00ae9676-2a48-4703-bc9d-0fed1d0537eb"), "pizza", "some sausage and pepperoni", OffsetDateTime.now()),
    Product(UUID.fromString("f783c79b-054b-4744-b50b-581b8f90db10"), "beer", "some delicious IPA", OffsetDateTime.now())
  )

  suspend fun getAllProducts(): List<Product> = products

  suspend fun getProduct(id: UUID): Product? = products.find { it.id == id }

  suspend fun saveProduct(product: Product): Either<String, Product> {
    return if (products.add(product))
      Either.right(product)
    else
      Either.left("Could not save the product")
  }

  fun deleteProducts(): Unit = products.clear()

}
