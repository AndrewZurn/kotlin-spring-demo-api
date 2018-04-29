package com.zalude.orders

import com.zalude.orders.controllers.ProductsController
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [(OrdersApiApplication::class)])
class SpringTest : WordSpec() {

  override fun listeners() = listOf(SpringListener)

  @Autowired
  var controller: ProductsController? = null

  init {
    "Spring Extension" should {
      "have wired up the bean" {
        controller shouldNotBe null
      }
    }
  }
}