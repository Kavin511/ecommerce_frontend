package com.devstudio.zivame.service

import com.devstudio.zivame.models.Product
import com.devstudio.zivame.repository.ProductRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProductService {
    @GET("nancymadan/assignment/db")
    fun fetchProduct(): Call<List<Product>>

    companion object {
        private lateinit var productService: ProductService
        private const val BASE_URL = "https://my-json-server.typicode.com/"
        fun getInstance(): ProductService {
            productService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(ProductRepository.client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductService::class.java)
            return productService
        }
    }
}