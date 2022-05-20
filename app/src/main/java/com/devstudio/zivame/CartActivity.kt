package com.devstudio.zivame

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstudio.zivame.adapters.CartListAdapter
import com.devstudio.zivame.models.ShoppingCartItem
import com.devstudio.zivame.repository.ShoppingCartRepository

class CartActivity : AppCompatActivity() {
    private lateinit var circularProgressBar: ProgressBar
    private lateinit var placeOrder: Button
    private lateinit var cartList: RecyclerView
    lateinit var successScreen: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initialise()
        initialiseCartList()
        initialiseActionBar()
        initialisePlaceOrder()
    }

    private fun initialise() {
        cartList = findViewById(R.id.cart_list)
        placeOrder = findViewById(R.id.place_order)
        circularProgressBar = findViewById(R.id.progress_circular)
        successScreen = findViewById(R.id.success_layout)
    }

    private fun initialisePlaceOrder() {
        placeOrder.setOnClickListener {
            if (shoppingCartRepository.isEmpty()) {
                Toast.makeText(this, "Please add items to cart", Toast.LENGTH_SHORT).show()
            } else {
                processOrder()
            }
        }
    }

    private val shoppingCartRepository = ShoppingCartRepository()

    private fun processOrder() {
        cartList.visibility = GONE
        circularProgressBar.visibility = VISIBLE
        supportActionBar?.hide()
        placeOrder.visibility = GONE
        Handler().postDelayed({
                                  shoppingCartRepository.clear()
                                  showSuccessScreen()
                              }, 30000)
    }

    private fun showSuccessScreen() {
        circularProgressBar.visibility = GONE
        successScreen.visibility = VISIBLE
        Handler().postDelayed({
                                  onBackPressed()
                              }, 1000)

    }

    private fun initialiseCartList() {
        cartList.adapter = CartListAdapter(shoppingCartRepository.findAll())
        cartList.layoutManager = GridLayoutManager(this, 1)
    }

    private fun initialiseActionBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar?.elevation = 0F
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val customView = supportActionBar!!.customView
        val title = customView.findViewById<TextView>(R.id.title)
        val cartBadge = customView.findViewById<FrameLayout>(R.id.cart_badge)
        title.text = "Cart"
        cartBadge.visibility = INVISIBLE
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(this, R.drawable.action_bar_background)
        )
    }

}