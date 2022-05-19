package com.devstudio.zivame.cart

import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ShoppingCart : LinkedHashMap<Long, ShoppingCartItem>(), Serializable {
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
