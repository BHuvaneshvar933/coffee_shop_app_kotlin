package com.example.delivery_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val description: String = "",
    val picUrl: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val categoryId: String = "",
    val numberInCart: Int = 1
)
