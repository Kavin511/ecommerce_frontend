package com.devstudio.zivame

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstudio.zivame.adapters.ProductListAdapter
import com.devstudio.zivame.listeners.OnItemClickListener
import com.devstudio.zivame.models.Product
import com.devstudio.zivame.models.ShoppingCartItem
import com.devstudio.zivame.repository.ProductRepository
import com.devstudio.zivame.repository.ShoppingCartRepository
import com.devstudio.zivame.service.ProductService
import com.devstudio.zivame.viewmodels.ProductViewModel
import com.devstudio.zivame.viewmodels.ProductViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var badgeText: TextView
    private lateinit var badgeLayout: RelativeLayout
    private lateinit var productList: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private lateinit var cartBadge:FrameLayout
    lateinit var progressBar:ProgressBar
    private fun initialiseCartMenu() {
        badgeLayout = findViewById(R.id.badge)
        cartBadge = badgeLayout.findViewById<FrameLayout>(R.id.cart_badge)
        cartBadge.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        badgeText = badgeLayout.findViewById<View>(R.id.actionbar_notifcation_textview) as TextView
    }

    private fun updateBadgeCount() {
        val size = shoppingCartRepository.getCartCount()
        badgeText.text = size.toString()
        if (size == 0) {
            badgeText.visibility = GONE
        } else {
            badgeText.visibility = VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialise()
        val viewModel = initialiseProductViewModel()
        observeProductList(viewModel)
        observeProductList(viewModel)
        initialiseProductList(viewModel)
    }

    private fun initialise() {
        productList = findViewById(R.id.products_list)
        progressBar = findViewById(R.id.progress_circular)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar?.elevation = 0F
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.action_bar_background
            )
        )
        initialiseCartMenu()
        updateBadgeCount()
    }

    private fun initialiseProductViewModel(): ProductViewModel {
        val productService = ProductService.getInstance()
        val viewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(ProductRepository(productService = productService))
        ).get(ProductViewModel::class.java)
        return viewModel
    }

    private fun observeProductList(viewModel: ProductViewModel) {
        viewModel.fetchProducts()
        viewModel.productList.observe(this) {
            progressBar.visibility = GONE
            adapter = ProductListAdapter(
                viewModel.productList.value ?: listOf(),
                applicationContext,
                productClickListener()
            )
            productList.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        updateBadgeCount()
    }

    private fun initialiseProductList(viewModel: ProductViewModel) {
        adapter = ProductListAdapter(
            viewModel.productList.value ?: listOf(),
            applicationContext,
            productClickListener()
        )
        productList.adapter = adapter
        productList.layoutManager = GridLayoutManager(this, 1)
    }

    private fun productClickListener() = object : OnItemClickListener {
        override fun onItemClick(item: Product) {
            addItemToShoppingCart(item)
        }
    }

    private val shoppingCartRepository = ShoppingCartRepository()

    private fun addItemToShoppingCart(item: Product) {
        val shoppingCartItem = ShoppingCartItem()
        shoppingCartItem.id = Calendar.getInstance().timeInMillis
        shoppingCartItem.name = item.name
        shoppingCartItem.rating = item.rating
        shoppingCartItem.price = item.price
        shoppingCartItem.imageUrl = item.imageUrl
        shoppingCartRepository.addProducts(shoppingCartItem)
        updateBadgeCount()
        showAddProductToCartAnimation()
    }

    private fun showAddProductToCartAnimation() {
       cartBadge.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))
    }

}