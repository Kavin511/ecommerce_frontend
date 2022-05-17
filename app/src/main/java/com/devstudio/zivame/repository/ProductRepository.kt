package com.devstudio.zivame.repository

import androidx.lifecycle.MutableLiveData
import com.devstudio.zivame.models.Product
import com.devstudio.zivame.service.ProductService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductRepository constructor(private val productService: ProductService) {

    public fun fetchProducts() = productService.fetchProduct()

    companion object {
        var productLiveData: MutableLiveData<Product> = MutableLiveData<Product>();
        val client: OkHttpClient = OkHttpClient.Builder().build();
    }
}