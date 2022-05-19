package com.devstudio.zivame.models

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("products")
    var products: List<Product>
)

data class Product(
    var name: String = "",
    var price: String = "",
    var imageUrl: String = "",
    var rating: Int = 0
)
