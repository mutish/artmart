package com.example.artmart.community.model

data class Message(
    val id: String,
    val text: String = "",
    val sender: String = "",
    val timestamp: Any = ""
)
