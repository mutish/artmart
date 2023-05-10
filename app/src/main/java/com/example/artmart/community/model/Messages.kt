package com.example.artmart.community.model

import java.sql.Timestamp

data class Messages(
    val text: String,
    val sender: String,
    val timestamp: Any
){
    constructor() : this("","","")
}

