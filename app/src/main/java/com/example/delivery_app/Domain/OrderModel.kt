package com.example.delivery_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    val id: String = "",
    val userId: String = "",
    val status: String = "Order Received",
    val totalPrice: Double = 0.0,
    val items: List<ItemsModel> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable
