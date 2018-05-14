package com.zalude.orders.controllers

import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest

/**
 * @author andrew.zurn@dexcom.com - 5/14/18.
 */
interface AppController {

  suspend fun <T> handleRequest(request: HttpServletRequest,
                                func: suspend (RequestContext) -> ResponseEntity<T>): ResponseEntity<T> {
    val traceId = request.getHeader("Trace-Id")
    val auth: String? = request.getHeader("Authorization")
    return func(RequestContext(traceId, auth))
  }

  data class RequestContext(val traceId: String, val auth: String?)

}
