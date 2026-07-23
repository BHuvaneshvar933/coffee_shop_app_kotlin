package com.example.delivery_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantModel(
    val id: String = "",
    val name: String = "",
    val coverImage: String = "",
    val logo: String = "",
    val rating: Double = 0.0,
    val deliveryTime: String = "",
    val deliveryFee: Double = 0.0,
    val distance: String = "",
    val cuisine: String = ""
) : Parcelable
