package com.devstudio.zivame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devstudio.zivame.repository.ProductRepository
import com.devstudio.zivame.service.ProductService
import com.devstudio.zivame.viewmodels.ProductViewModel
import com.devstudio.zivame.viewmodels.ProductViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val productService = ProductService.getInstance()
        val viewModel = ViewModelProvider(this, ProductViewModelFactory(ProductRepository(productService = productService))).get(ProductViewModel::class.java)
        viewModel.productList.observe(this) {
            Log.d("product response", "onCreate: $it")
        }
        findViewById<TextView>(R.id.textview).setOnClickListener {
            viewModel.fetchProducts()
        }
    }
}