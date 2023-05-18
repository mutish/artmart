package com.example.artmart

import androidx.annotation.DrawableRes

data class Categories(
    val id:Long,
    val name:String,
    @DrawableRes val imageRes:Int,
    val info: String
)
