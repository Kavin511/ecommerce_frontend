package com.devstudio.zivame

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstudio.zivame.cart.ShoppingCart
import com.devstudio.zivame.cart.ShoppingCartItem
import com.devstudio.zivame.models.Product
import com.devstudio.zivame.repository.ProductRepository
import com.devstudio.zivame.service.ProductService
import com.devstudio.zivame.viewmodels.ProductViewModel
import com.devstudio.zivame.viewmodels.ProductViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var productList: RecyclerView
    private lateinit var adapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val productService = ProductService.getInstance()
        val viewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(ProductRepository(productService = productService))
        ).get(ProductViewModel::class.java)
        productList = findViewById(R.id.products_list)
        viewModel.productList.observe(this) {
            adapter = ProductListAdapter(
                viewModel.productList.value ?: listOf(),
                applicationContext,
                productClickListener()
            )
            productList.adapter = adapter
        }
        viewModel.fetchProducts()
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
            val cart = ShoppingCart.getInstance()
            var position: Long = cart?.size?.toLong() ?: 0;
            cart?.forEach { _, shoppingCartItem ->
                if (shoppingCartItem.id == item.id) {
                    position = shoppingCartItem.position
                    return@forEach
                }
            }
            val item: ShoppingCartItem =
                cart?.get(position) ?: ShoppingCartItem(item.id!!, 0, position)
            item.quantity += 1
            cart?.put(position, item)
            Log.d("TAG", "onItemClick: " + cart.toString())
        }
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