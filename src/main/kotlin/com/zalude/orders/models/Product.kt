package com.zalude.orders.models

import java.util.UUID

/**
 * @author awzurn@gmail.com - 4/15/18.
 */
data class Product(val id: UUID,
                   val name: String,
                   val description: String)