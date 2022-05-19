package com.devstudio.zivame.cart

import androidx.lifecycle.ViewModel
import com.devstudio.zivame.models.Product
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ShoppingCart : HashSet<Product>(), Serializable {
    fun addItem(item: Product) {
        add(item)
    }

    companion object {
        private var instance: ShoppingCart? = null
        fun getInstance(): ShoppingCart? {
            if (instance == null) {
                instance = ShoppingCart()
            }
            return instance
        }
    }
}
