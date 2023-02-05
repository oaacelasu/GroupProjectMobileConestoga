package com.conestoga.groupproject.data.entities

import java.io.Serializable

data class Product(
    val brand: String = "",
    val description: String = "",
    val image: String = "",
    val model: String = "",
    val price: Double = 0.0,
) : Serializable