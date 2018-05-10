package com.zalude.orders

import com.zalude.orders.controllers.ProductsController
import com.zalude.orders.models.Product
import com.zalude.orders.services.ProductsService
import io.kotlintest.matchers.containAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import kotlinx.coroutines.experimental.*

import java.util.*

/**
 * @author awzurn@gmail.com - 4/23/18.
 */
@ContextConfiguration(classes = [(OrdersApiApplication::class)])
class SpringIntegrationTest : WordSpec() {

  override fun listeners() = listOf(SpringListener)

  @Autowired
  lateinit var controller: ProductsController

  @Autowired
  lateinit var service: ProductsService

  init {
    "The Product Controller" should {

      val testProducts = listOf(
          Product(UUID.fromString("e644d8a3-30e9-40ed-ae2e-56883afac7ce"), "Pizza", "Great with beer"),
          Product(UUID.fromString("6c31228d-32e9-427a-9724-504e2746acbb"), "Beer", "Great with pizza")
      )
      service.deleteProducts()
      testProducts.forEach { runBlocking { service.saveProduct(it) } }

      "get a list of products, returning a 200 response" {
        val result = runBlocking { controller.getProduct() }
        result.statusCodeValue shouldBe 200
        val products = result.body
        products.size shouldBe 2
        result.body should containAll(products)
      }

      "get a specific product, returning a 200 response" {
        val result = runBlocking { controller.getProduct(testProducts.first().id) }
        result.statusCodeValue shouldBe 200
        result.body shouldBe testProducts.first()
      }

      "not find a product, returning a 404 response" {
        val result = runBlocking { controller.getProduct(UUID.randomUUID()) }
        result.statusCodeValue shouldBe 404
      }

      "save a product, returning a 201 response" {
        val productRequest = ProductsController.ProductRequest("Chicken Wings", "With your pizza and beer")
        val result = runBlocking { controller.createProduct(productRequest) }
        result.statusCodeValue shouldBe 201
        val savedProduct = result.body as Product
        savedProduct.id shouldNotBe null
        savedProduct.name shouldBe productRequest.name
        savedProduct.description shouldBe productRequest.description
      }
    }
  }
}