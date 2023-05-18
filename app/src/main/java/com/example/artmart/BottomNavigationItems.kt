package com.example.artmart

sealed class BottomNavigationItems(var route: String, var icon: Int, var title: String ){
    object Home : BottomNavigationItems("home", R.drawable.home, "Home")
    object Class: BottomNavigationItems("class",R.drawable.brush,"Resources")
}
