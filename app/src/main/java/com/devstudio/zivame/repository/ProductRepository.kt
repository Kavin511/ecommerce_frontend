package com.devstudio.zivame.repository

import androidx.lifecycle.MutableLiveData
import com.devstudio.zivame.models.ProductsResponse
import com.devstudio.zivame.service.ProductService
import okhttp3.OkHttpClient

class ProductRepository constructor(private val productService: ProductService) {

    public fun fetchProducts() = productService.fetchProduct()

    companion object {
        var productLiveData: MutableLiveData<ProductsResponse> = MutableLiveData<ProductsResponse>();
        val client: OkHttpClient = OkHttpClient.Builder().build();
    }
}