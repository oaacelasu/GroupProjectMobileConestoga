package com.conestoga.groupproject.data.entities

import java.io.Serializable
import java.util.*

data class OrderItem(
    var product:Product? = null,
    var quantity: Int = 0,
) : Serializable