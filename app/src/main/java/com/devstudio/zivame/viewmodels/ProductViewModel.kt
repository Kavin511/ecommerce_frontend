package com.devstudio.zivame.viewmodels

import android.telecom.Call
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devstudio.zivame.models.Product
import com.devstudio.zivame.repository.ProductRepository
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel constructor(private val repository: ProductRepository) : ViewModel() {
    val productList = MutableLiveData<List<Product>>()
    val errorMessage = MutableLiveData<String>()
    fun fetchProducts() {
        val response = repository.fetchProducts()
        response.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: retrofit2.Call<List<Product>>, response: Response<List<Product>>) {
                productList.postValue(response.body())
            }

            override fun onFailure(call: retrofit2.Call<List<Product>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }
}