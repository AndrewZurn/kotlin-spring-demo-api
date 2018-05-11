package com.zalude.orders.models.web

import com.zalude.orders.models.domain.Product

/**
 * @author andrew.zurn@dexcom.com - 5/10/18.
 */
data class ProductResponseWrapper(val products: List<Product>)
