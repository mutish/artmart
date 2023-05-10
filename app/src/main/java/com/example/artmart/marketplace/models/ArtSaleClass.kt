package com.example.artmart.marketplace.models

data class ArtSaleClass(
    var artid: String,
    var image: String,
    var title: String,
    var artist: String,
    var price: String,
    var phonenumber: String
        ){
    constructor() : this ("", "", "", "", "","")
}
