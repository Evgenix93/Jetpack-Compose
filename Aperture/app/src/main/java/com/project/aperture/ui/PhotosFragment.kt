package com.project.aperture.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.project.aperture.*
import com.project.aperture.databinding.PhotosFragmentBinding
import com.project.aperture.utils.AutoClearedValue
import com.project.aperture.utils.textChangedFlow

class PhotosFragment : Fragment(R.layout.photos_fragment) {
    private val binding: PhotosFragmentBinding by viewBinding()
    private var photosAdapter by AutoClearedValue<PhotosAdapter>()
    private val viewModel: PhotosFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 1000
        }
        viewModel.getFirstPhotos()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           postponeEnterTransition()
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation(true)
        photosAdapter = PhotosAdapter(false, {photoId, itemView ->
            val extras = FragmentNavigatorExtras(itemView to "photoDetails")
            findNavController().navigate(PhotosFragmentDirections.actionPhotosFragmentToPhotoFragment(photoId), extras)
        }, { position, likedByUser ->
            viewModel.likePhoto(position, likedByUser)
        }, {
            viewModel.getPhotos()
        })
        with(binding.photosRecyclerView) {
            adapter = photosAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            addItemDecoration(PhotoListDecoration())
        }
        viewModel.startFragmentLiveData.observe(viewLifecycleOwner) {
                findNavController().navigate(PhotosFragmentDirections.actionPhotosFragmentToStartFragment())
        }

        viewModel.photosLiveData.observe(viewLifecycleOwner) {
            photosAdapter.updateList(it)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.photosDBLiveData.observe(viewLifecycleOwner) {
            photosAdapter.updateListDB(it)
        }

        viewModel.noConnectionLiveData.observe(viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = false
            val text = if (it)
                getString(R.string.noConnection2)
            else
                getString(R.string.noConnection3)
            Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(requireContext(), R.color.translucent2))
                .setTextColor(getColor(requireContext(), R.color.red))
                .show()

        }

        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            if (it != null)
            photosAdapter.updatePhoto(it.first, it.second)
        }

        val searchView = binding.materialToolbar.menu.findItem(R.id.searchItem).actionView as SearchView
        viewModel.searchPhotos(searchView.textChangedFlow())

        binding.swipeRefreshLayout.setOnRefreshListener{
            viewModel.clearPage()
            viewModel.getPhotos()
        }

        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelCoroutine()
    }
}
