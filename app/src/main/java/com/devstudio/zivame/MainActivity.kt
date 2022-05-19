package com.devstudio.zivame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstudio.zivame.cart.ShoppingCart
import com.devstudio.zivame.models.Product
import com.devstudio.zivame.repository.ProductRepository
import com.devstudio.zivame.service.ProductService
import com.devstudio.zivame.viewmodels.ProductViewModel
import com.devstudio.zivame.viewmodels.ProductViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var badgeText: TextView
    private lateinit var badgeLayout: RelativeLayout
    private lateinit var productList: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private var cartMenu: Menu? = null

    private fun initialiseCartMenu() {
        badgeLayout = findViewById(R.id.badge)
        val cartBadge = badgeLayout.findViewById<FrameLayout>(R.id.cart_badge)
        cartBadge.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        badgeText = badgeLayout.findViewById<View>(R.id.actionbar_notifcation_textview) as TextView
    }

    private fun updateBadgeCount() {
        val size = ShoppingCart.getInstance()?.size
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
        productList.layoutManager = GridLayoutManager(this, 2)
    }

    private fun productClickListener() = object : OnItemClickListener {
        override fun onItemClick(item: Product) {
            addItemToShoppingCart(item)
        }
    }

    private fun addItemToShoppingCart(item: Product) {
        val cart = ShoppingCart.getInstance()
        cart?.addItem(item)
        updateBadgeCount()
    }

    internal class ProductListAdapter(
        private val products: List<Product>,
        private val context: Context,
        private val listener: OnItemClickListener
    ) :
        RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var productName: TextView = view.findViewById(R.id.product_name) as TextView
            var productImage: ImageView = view.findViewById(R.id.product_image) as ImageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context: Context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.product_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val product = products[position]
            holder.productName.text = product.name
            Glide.with(context.applicationContext)
                .asBitmap()
                .load(product.imageUrl)
                .optionalFitCenter()
                .error(ContextCompat.getDrawable(context, R.drawable.ic_image))
                .into(holder.productImage);
            holder.itemView.setOnClickListener { listener.onItemClick(product) }
        }

        override fun getItemCount(): Int {
            return products.size
        }
    }

}

interface OnItemClickListener {
    fun onItemClick(item: Product)
}