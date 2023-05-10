package com.example.artmart.gallery.model

data class ArtPieces(
    var galid: String,
    var category: String,
    var title: String,
    var artist: String,
    var description: String
){
    constructor() : this("","","","","")
}

