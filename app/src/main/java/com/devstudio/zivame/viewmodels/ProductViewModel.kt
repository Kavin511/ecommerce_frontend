package com.devstudio.zivame.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devstudio.zivame.models.Product
import com.devstudio.zivame.models.ProductsResponse
import com.devstudio.zivame.repository.ProductRepository
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel constructor(private val repository: ProductRepository) : ViewModel() {
    val productList = MutableLiveData<List<Product>?>()
    val errorMessage = MutableLiveData<String>()
    fun fetchProducts() {
        val response = repository.fetchProducts()
        response.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: retrofit2.Call<ProductsResponse>, response: Response<ProductsResponse>) {
                productList.postValue((response.body() as ProductsResponse).products)
            }

            override fun onFailure(call: retrofit2.Call<ProductsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }
}