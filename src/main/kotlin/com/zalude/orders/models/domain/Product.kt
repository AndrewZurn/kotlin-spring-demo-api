package com.zalude.orders.models.domain

import java.time.OffsetDateTime
import java.util.UUID

/**
 * @author awzurn@gmail.com - 4/15/18.
 */
data class Product(val id: UUID,
                   val name: String,
                   val description: String,
                   val createdAt: OffsetDateTime)
