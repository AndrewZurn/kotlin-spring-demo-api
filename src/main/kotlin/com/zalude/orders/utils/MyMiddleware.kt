package com.zalude.orders.utils

import mu.KLogging
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author andrew.zurn@dexcom.com - 5/10/18.
 */
@Component
class LoggingFilter : OncePerRequestFilter() {

  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val start = System.currentTimeMillis()
    filterChain.doFilter(request, response)
    logger.info("${request.method} ${request.requestURI} took ${System.currentTimeMillis() - start}ms, resulted in status: ${response.status}")
  }

  companion object: KLogging()

}
