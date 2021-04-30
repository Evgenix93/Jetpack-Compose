package com.skillbox.homework32_5

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.skillbox.homework32_5.databinding.ProductItemBinding

class ProductAdapter(private val context: Context, private val onClick: (MaterialCardView, Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.Holder>() {

    private var products = emptyList<Product>()

    class Holder(private val context: Context, view: View, private val onClick: (MaterialCardView, Product) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding by viewBinding<ProductItemBinding>()

        fun bind(product: Product) {
            binding.root.transitionName = "product item ${product.name}"
            binding.root.setOnClickListener { onClick(binding.root, product) }
            binding.titleTextView.text = product.name
            binding.priceTextView.text = context.getString(R.string.price, product.price)

            Glide.with(binding.root)
                .load(product.image)
                .error(R.drawable.error)
                .into(binding.pictureImageView)
        }
    }

    fun update(list: List<Product>) {
        products = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(context, parent.inflate(R.layout.product_item), onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }
}