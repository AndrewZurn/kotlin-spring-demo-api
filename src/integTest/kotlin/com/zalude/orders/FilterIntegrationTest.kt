package com.zalude.orders

import com.zalude.orders.controllers.InfoController
import com.zalude.orders.utils.TraceIdHeaderKey
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

/**
 * @author andrew.zurn@dexcom.com - 5/14/18.
 */
@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [InfoController::class])
class FilterIntegrationTest {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Test
  @Throws(Exception::class)
  fun `Should return a Trace-Id in the headers if initially present in the request`() {
    val testTraceId = "SomeRandomTraceId"
    this.mockMvc
      .perform(get("/info").header(TraceIdHeaderKey, testTraceId))
      .andDo(print())
      .andExpect(status().isOk)
      .andExpect(header().string(TraceIdHeaderKey, testTraceId))
  }
}
