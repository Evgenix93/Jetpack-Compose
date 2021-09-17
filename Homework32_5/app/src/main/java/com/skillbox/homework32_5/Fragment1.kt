package com.skillbox.homework32_5

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.skillbox.homework32_5.databinding.Fragment1Binding

class Fragment1 : Fragment(R.layout.fragment1) {
    private val viewModel by viewModels<ProductListViewModel>()
    private val binding by viewBinding<Fragment1Binding>()
    private var productAdapter: ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = ProductAdapter(requireContext()) { cardView, product ->
            val extras = FragmentNavigatorExtras(cardView to "product details")
            val direction = Fragment1Directions.actionProductListToProductDetails(product)
            findNavController().navigate(direction, extras)
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300.toLong()
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300.toLong()
        }

        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300.toLong()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("MyTag", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.productList.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            addItemDecoration(ItemOffset())
        }

        viewModel.products.observe(viewLifecycleOwner) {
            productAdapter?.update(it)
        }

        viewModel.getProducts()

        Snackbar.make(
            binding.productList,
            "Соединение с сервером отсутствует, показаны сохранённые объекты",
            Snackbar.LENGTH_LONG
        )
            .setAction("Повторить") {
                Snackbar.make(binding.productList, "Список обновлен", Snackbar.LENGTH_SHORT)
                    .show()
            }
            .setMaxInlineActionWidth(30)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productAdapter = null
    }
}