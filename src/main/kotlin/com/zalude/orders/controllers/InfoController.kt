package com.zalude.orders.controllers

import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * @author andrew.zurn@dexcom.com - 5/14/18.
 */
@RestController
open class InfoController: AppController {

  @GetMapping("/info")
  suspend fun info(request: HttpServletRequest): ResponseEntity<AppInfo> = handleRequest(request) {
    logger.info { "The request context is: $it" }
    ResponseEntity(AppInfo("Something about info"), HttpStatus.OK)
  }

  data class AppInfo(val info: String)

  companion object: KLogging()
}
