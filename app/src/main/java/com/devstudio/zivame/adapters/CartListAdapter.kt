package com.devstudio.zivame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devstudio.zivame.R
import com.devstudio.zivame.models.ShoppingCartItem

class CartListAdapter(private val products:List<ShoppingCartItem>) :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

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
        val product = products[position]
        holder.productName.text = product.name
        holder.productPrice.text = "$"+product.price
    }

    override fun getItemCount(): Int {
        return products.size
    }
}