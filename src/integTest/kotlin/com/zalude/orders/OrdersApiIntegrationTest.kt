package com.zalude.orders

import com.zalude.orders.controllers.ProductsController
import com.zalude.orders.models.domain.Product
import com.zalude.orders.models.web.CreateProductRequest
import com.zalude.orders.models.web.ErrorWrapper
import com.zalude.orders.models.web.ProductResponseWrapper
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
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.validation.BeanPropertyBindingResult
import java.time.OffsetDateTime

import java.util.*

/**
 * @author awzurn@gmail.com - 4/23/18.
 */
@ContextConfiguration(classes = [(OrdersApiApplication::class)])
class OrdersApiIntegrationTest : WordSpec() {

  override fun listeners() = listOf(SpringListener)

  @Autowired
  lateinit var controller: ProductsController

  @Autowired
  lateinit var service: ProductsService

  init {
    "The Product Controller" should {

      val mockRequest = MockHttpServletRequest()
      val testProducts = listOf(
        Product(UUID.fromString("e644d8a3-30e9-40ed-ae2e-56883afac7ce"), "Pizza", "Great with beer", OffsetDateTime.now()),
        Product(UUID.fromString("6c31228d-32e9-427a-9724-504e2746acbb"), "Beer", "Great with pizza", OffsetDateTime.now())
      )
      service.deleteProducts()
      testProducts.forEach { runBlocking { service.saveProduct(it) } }

      "get a list of products, returning a 200 response" {
        val result: ResponseEntity<ProductResponseWrapper> = runBlocking { controller.getProduct() }
        result.statusCodeValue shouldBe 200
        val products = result.body!!.products
        products.size shouldBe 2
        products should containAll(products)
      }

      "get a specific product, returning a 200 response" {
        val result = runBlocking { controller.getProduct(testProducts.first().id, mockRequest) }
        result.statusCodeValue shouldBe 200
        result.body shouldBe testProducts.first()
      }

      "not find a product, returning a 404 response" {
        val result = runBlocking { controller.getProduct(UUID.fromString("be827ea4-97e6-41e9-958f-23c91e991450"), mockRequest) }
        result.statusCodeValue shouldBe 404
      }

      "save a product, returning a 201 response" {
        val productRequest = CreateProductRequest("Chicken Wings", "With your pizza and beer")
        val result = runBlocking { controller.createProduct(productRequest, BeanPropertyBindingResult(productRequest, "")) }
        result.statusCodeValue shouldBe 201
        val savedProduct = result.body as Product
        savedProduct.id shouldNotBe null
        savedProduct.name shouldBe productRequest.name
        savedProduct.description shouldBe productRequest.description
      }

      "return a 400 if an invalid create product request is sent" {
        val invalidProductRequest = CreateProductRequest("", "")
        val result = runBlocking {
          controller.createProduct(invalidProductRequest, BeanPropertyBindingResult(invalidProductRequest, "productRequest"))
        }
        result.statusCodeValue shouldBe 400
        val errorWrapper = result.body as ErrorWrapper
        errorWrapper.errors.size shouldBe 2
        errorWrapper.errors.map { it.field } should containAll("name", "description")
        errorWrapper.errors.map { it.message } should containAll("Cannot be blank or null.")
      }
    }
  }
}
