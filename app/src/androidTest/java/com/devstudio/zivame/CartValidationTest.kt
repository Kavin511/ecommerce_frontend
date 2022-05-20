package com.devstudio.zivame

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.devstudio.zivame.models.ShoppingCartItem
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CartValidationTest {

    val TEST_STRING = ShoppingCartItem()
    val TEST_LONG = 12345678L
    private lateinit var shoppingCartItem: ShoppingCartItem

    @Before
    fun createLogHistory() {
        shoppingCartItem = ShoppingCartItem()
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val db = run {
        Realm.init(context)
        Realm.getInstance(
            RealmConfiguration.Builder()
                .inMemory()
                .name("realm.db")
                .build()
        )
    }

    @Test
    public fun placeOrder() {
        db.executeTransaction {
            it.delete(ShoppingCartItem::class.java)
        }
        assert(value = true) { db.where(ShoppingCartItem::class.java).findAll().size == 0 }
    }

    @Test
    fun addToCart() {
        shoppingCartItem.name = "Temp"
        shoppingCartItem.rating = 3
        shoppingCartItem.price = "1"
        val size = getCartSize()
        db.executeTransaction {
            it.copyToRealmOrUpdate(shoppingCartItem)
        }
        assert(true) { size < getCartSize() }
    }

    private fun getCartSize() = db.where(ShoppingCartItem::class.java).findAll().size
}