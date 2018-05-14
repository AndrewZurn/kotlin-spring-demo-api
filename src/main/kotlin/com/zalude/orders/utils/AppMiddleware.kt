package com.zalude.orders.utils

import mu.KLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.http.HttpServletRequestWrapper

/**
 * @author andrew.zurn@dexcom.com - 5/10/18.
 */
const val TraceIdHeaderKey = "Trace-Id"

@Component
class AppMiddleware : OncePerRequestFilter() {

  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val start = System.currentTimeMillis()
    val updatedRequest = request.setTraceIdIfNotPresent()
    response.setHeader(TraceIdHeaderKey, updatedRequest.getHeader(TraceIdHeaderKey))
    filterChain.doFilter(updatedRequest, response)
    logger.info("${updatedRequest.method} ${updatedRequest.requestURI} took ${System.currentTimeMillis() - start}ms, resulted in status: ${response.status}")
  }


  companion object: KLogging()

}

fun HttpServletRequest.setTraceIdIfNotPresent(): HttpServletRequestWrapper {
  return object : HttpServletRequestWrapper(this) {
    override fun getHeader(name: String): String? {
      return if (name == TraceIdHeaderKey && super.getHeader(name) == null)
        RandomStringUtils.random(16, true, true)
      else
        super.getHeader(name)
    }
  }
}
