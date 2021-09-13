package com.project.aperture.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.project.aperture.R
import com.project.aperture.databinding.CollectionsFragmentBinding
import com.project.aperture.utils.AutoClearedValue

class CollectionsFragment : Fragment(R.layout.collections_fragment) {
    private val binding: CollectionsFragmentBinding by viewBinding()
    private val viewModel: CollectionsFragmentViewModel by viewModels()
    private var collectionsAdapter by AutoClearedValue<CollectionsAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 1000
        }
        viewModel.getCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        collectionsAdapter = CollectionsAdapter (requireContext(), {collectionId, itemView ->
            val extras = FragmentNavigatorExtras(itemView to "collectionDetails")
            findNavController().navigate(
                CollectionsFragmentDirections.actionCollectionsFragmentToCollectionFragment(
                    collectionId
                ), extras)
        }, {viewModel.getCollections()})

        with(binding.collectionsRecyclerView) {
            adapter = collectionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.collectionsLiveData.observe(viewLifecycleOwner) {
            collectionsAdapter.updateList(it)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.collectionsDBLiveData.observe(viewLifecycleOwner) {
            collectionsAdapter.updateListDB(it)

        }

        viewModel.noConnectionLiveData.observe(viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = false
            val text = if (it)
                getString(R.string.noConnection2)
            else
                getString(R.string.noConnection4)
            Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(requireContext(), R.color.translucent2))
                .setTextColor(getColor(requireContext(), R.color.red))
                .show()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.clearPage()
            viewModel.getCollections()
        }
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}