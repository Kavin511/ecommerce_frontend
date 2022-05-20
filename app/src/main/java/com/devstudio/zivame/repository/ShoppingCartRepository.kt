package com.devstudio.zivame.repository

import com.devstudio.zivame.models.ShoppingCartItem
import io.realm.Realm
import io.realm.kotlin.delete

class ShoppingCartRepository {
    private val realm: Realm = Realm.getDefaultInstance()
    fun addProducts(shoppingCartItem: ShoppingCartItem) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(shoppingCartItem)
        }
    }

    fun findAll(): List<ShoppingCartItem> {
        return realm.where(ShoppingCartItem::class.java).findAll().toList()
    }

    fun getCartCount(): Int {
        return realm.where(ShoppingCartItem::class.java).findAll().size
    }

    fun clear() {
        realm.executeTransaction {
            it.delete<ShoppingCartItem>()
        }
    }

    fun isEmpty(): Boolean {
        return getCartCount() == 0
    }
}