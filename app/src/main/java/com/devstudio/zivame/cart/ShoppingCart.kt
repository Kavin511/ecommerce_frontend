package com.devstudio.zivame.cart

import com.devstudio.zivame.models.Product
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ShoppingCart : LinkedHashMap<Long, ShoppingCartItem>(), Serializable {
    fun addItem(item: Product) {
        val position = findItemPositionInCart(item)
        val shoppingCartItem: ShoppingCartItem = get(position) ?: ShoppingCartItem(item.id!!, 0, position)
        shoppingCartItem.quantity += 1
        put(position, shoppingCartItem)
    }

    private fun findItemPositionInCart(item: Product): Long {
        var position: Long = size.toLong()
        forEach { _, shoppingCartItem ->
            if (shoppingCartItem.id == item.id) {
                position = shoppingCartItem.position
                return@forEach
            }
        }
        return position
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
