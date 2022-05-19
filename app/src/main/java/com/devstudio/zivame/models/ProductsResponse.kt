package com.devstudio.zivame.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class ProductsResponse(
    @SerializedName("products")
    var products: List<Product>
)

data class Product(
    var id: UUID? =UUID.randomUUID(),
    var name: String = "",
    var price: String = "",
    @SerializedName("image_url")
    var imageUrl: String = "",
    var rating: Int = 0
)
