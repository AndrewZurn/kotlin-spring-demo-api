package com.zalude.orders.validators

import com.zalude.orders.models.web.CreateProductRequest
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

/**
 * @author andrew.zurn@dexcom.com - 5/10/18.
 */
@Component
class ProductRequestValidator {

  fun validate(target: Any?, errors: Errors) {
    val productRequest = target as CreateProductRequest
    if (productRequest.name.isBlank())
      errors.rejectValue("name", "EMPTY_OR_NULL_NAME", "Cannot be blank or null.")
    if (productRequest.description.isBlank())
      errors.rejectValue("description", "EMPTY_OR_NULL_DESCRIPTION", "Cannot be blank or null.")
  }

}
