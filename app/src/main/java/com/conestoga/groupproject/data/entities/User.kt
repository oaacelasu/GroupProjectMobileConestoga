package com.conestoga.groupproject.data.entities

import java.io.Serializable
import java.util.*

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
) : Serializable