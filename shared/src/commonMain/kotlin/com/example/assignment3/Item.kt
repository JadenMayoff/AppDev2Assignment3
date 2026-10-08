package com.example.assignment3

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
)