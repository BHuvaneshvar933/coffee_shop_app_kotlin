package com.example.delivery_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemsModel(
    var title : String="",
    var description : String="",
    var picUrl : ArrayList<String> = ArrayList(),
    var price : Double=0.0,
    var rating : Double=0.0,
    var numberInCart : Int = 0,
    var extra : String = "",
    var restaurantId : String = "",
    var categoryId : String = ""
) : Parcelable

