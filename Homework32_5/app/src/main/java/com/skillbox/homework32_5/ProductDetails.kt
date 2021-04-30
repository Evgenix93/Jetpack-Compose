package com.skillbox.homework32_5

import android.graphics.Color
import android.os.Bundle

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.skillbox.homework32_5.databinding.ProductDetailsBinding

class ProductDetails: Fragment(R.layout.product_details) {
   private val args: ProductDetailsArgs by navArgs()
   private val binding by viewBinding<ProductDetailsBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300.toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.titleDetailsTextView.text = args.product.name
        binding.priceDetailsTextView.text = args.product.price.toString()

        Glide.with(binding.root)
            .load(args.product.image)
            .error(R.drawable.error)
            .into(binding.pictureDetailsImageView)
    }
}