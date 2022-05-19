package com.devstudio.zivame

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstudio.zivame.cart.ShoppingCart
import com.devstudio.zivame.models.Product
import com.google.android.material.progressindicator.CircularProgressIndicator

class CartActivity : AppCompatActivity() {
    private lateinit var circularProgressBar: ProgressBar
    private lateinit var placeOrder: Button
    private lateinit var cartList: RecyclerView
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
    }

    private fun initialisePlaceOrder() {
        placeOrder.setOnClickListener {
            cartList.visibility = GONE
            circularProgressBar.visibility = VISIBLE
            placeOrder.visibility = GONE
            Handler().postDelayed({
                                      ShoppingCart.getInstance()?.clear()
                                      onBackPressed()
                                  }, 3000)
        }
    }

    private fun initialiseCartList() {
        cartList.adapter = CartListAdapter()
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
        title.text = "Checkout Cart"
        cartBadge.visibility = INVISIBLE
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(this, R.drawable.action_bar_background)
        )
    }

    internal class CartListAdapter :
        RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
        private val products: List<Product>? = ShoppingCart.getInstance()?.toList()

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var productName: TextView = view.findViewById(R.id.name) as TextView
            var productPrice: TextView = view.findViewById(R.id.price) as TextView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context: Context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.shopping_cart_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val product = products?.get(position)!!
            holder.productName.text = product.name
            holder.productPrice.text = product.price
        }

        override fun getItemCount(): Int {
            return ShoppingCart.getInstance()?.size ?: 0
        }
    }
}