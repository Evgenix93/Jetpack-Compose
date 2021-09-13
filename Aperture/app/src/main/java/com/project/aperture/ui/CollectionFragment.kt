package com.project.aperture.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.project.aperture.R
import com.project.aperture.databinding.CollectionFragmentBinding

import com.project.aperture.utils.AutoClearedValue
import java.io.File


class CollectionFragment: Fragment(R.layout.collection_fragment) {
    private val viewModel: CollectionFragmentViewModel by viewModels()
    private var photosAdapter by AutoClearedValue<PhotosAdapter>()
    private val binding: CollectionFragmentBinding by viewBinding()
    private val args: CollectionFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 700
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 1000
        }
        viewModel.getCollection(args.collectionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        photosAdapter = PhotosAdapter(
            true,
            {photoId, itemView ->
                val extras = FragmentNavigatorExtras(itemView to "photoDetails")
                findNavController().navigate(CollectionFragmentDirections.actionCollectionFragmentToPhotoFragment(photoId), extras)
            }, {position, likedByUser ->
                viewModel.likePhoto(position, likedByUser)
            }, {viewModel.getPhotos(args.collectionId)})

        with(binding.photosRecyclerView){
            adapter = photosAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.collectionLiveData.observe(viewLifecycleOwner){
            binding.materialToolbar.title = it.title
            viewModel.getPhoto(it.coverPhoto?.id)
            binding.titleTextView.text = it.title
            binding.descriptionTextView.text = it.description
            binding.photoCount.text = "${it.totalPhotos} images by @${it.user.userName}"
            Glide.with(binding.root)
                .load(it.coverPhoto?.urls?.get("raw") ?: "")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadingProgressBar.isVisible = false
                        binding.sadImageView.isVisible = true
                        binding.errorTextView.isVisible = true
                        return true
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadingProgressBar.isVisible = false
                        binding.coverPhoto.setImageDrawable(resource)
                        return true
                    }
                })
                .into(binding.coverPhoto)
        }

        viewModel.collectionDBLiveData.observe(viewLifecycleOwner){
            binding.titleTextView.text = it.title
             binding.descriptionTextView.text = it.description
            binding.photoCount.text = "${it.totalPhotos} images by @${it.userName}"
            Glide.with(binding.root)
                .load(File(it.coverPhotoPath))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadingProgressBar.isVisible = false
                        binding.sadImageView.isVisible = true
                        binding.errorTextView.isVisible = true
                        return true
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadingProgressBar.isVisible = false
                        binding.coverPhoto.setImageDrawable(resource)
                        return true
                    }
                })
                .into(binding.coverPhoto)
        }

        viewModel.coverPhotoLiveData.observe(viewLifecycleOwner){photo ->
            if (photo != null) {
                val tagsString = "#${
                    photo.tags?.joinToString(" #") { tag ->
                        tag.title
                    } ?: ""
                }"
                binding.tagsTextView.text = tagsString
            }
        }

        viewModel.photosLiveData.observe(viewLifecycleOwner){
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
            binding.swipeRefreshLayout.isRefreshing = false
        }


        viewModel.photoLiveData.observe(viewLifecycleOwner){(photo, position) ->
            photosAdapter.updatePhoto(photo, position)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPhotos(args.collectionId)
        }
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}