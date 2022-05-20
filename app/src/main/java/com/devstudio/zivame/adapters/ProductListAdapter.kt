package com.devstudio.zivame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstudio.zivame.R
import com.devstudio.zivame.listeners.OnItemClickListener
import com.devstudio.zivame.models.Product
import com.google.android.material.button.MaterialButton

class ProductListAdapter(
    private val products: List<Product>,
    private val context: Context,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productName: TextView = view.findViewById(R.id.product_name) as TextView
        var productImage: ImageView = view.findViewById(R.id.product_image) as ImageView
        var rating: RatingBar = view.findViewById(R.id.rating) as RatingBar
        var addButton: MaterialButton = view.findViewById(R.id.add_item) as MaterialButton
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
        holder.rating.rating= product.rating.toFloat()
        Glide.with(context.applicationContext)
            .asBitmap()
            .load(product.imageUrl)
            .optionalFitCenter()
            .error(ContextCompat.getDrawable(context, R.drawable.ic_image))
            .into(holder.productImage);
        holder.addButton.setOnClickListener { listener.onItemClick(product) }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}