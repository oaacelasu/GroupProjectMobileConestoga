package com.conestoga.groupproject.data.entities

import java.io.Serializable
import java.util.*

data class AppOrder(
    var items: List<OrderItem> = emptyList(),
    var totalPrice: Double = 0.0
) : Serializable