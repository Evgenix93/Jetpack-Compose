package com.project.aperture.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.project.aperture.R
import com.project.aperture.databinding.ProfileFragmentBinding
import com.project.aperture.utils.AutoClearedValue

class ProfileFragment : Fragment(R.layout.profile_fragment) {
    private val viewModel: ProfileFragmentViewModel by viewModels()
    private val binding: ProfileFragmentBinding by viewBinding()
    private var photosAdapter by AutoClearedValue<PhotosAdapter>()
    private var collectionsAdapter by AutoClearedValue<CollectionsAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 1000
        }
        viewModel.getProfile()
        viewModel.getProfilePhotos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        photosAdapter = PhotosAdapter(true, {photoId, itemView ->
            val extras = FragmentNavigatorExtras(itemView to "photoDetails")
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPhotoFragment(photoId), extras)
        }, { position, likedByUser ->
            viewModel.likePhoto(position, likedByUser)
        }, {
            viewModel.morePhotos()
        })
        collectionsAdapter = CollectionsAdapter (requireContext(), {collectionId, itemView ->
            val extras = FragmentNavigatorExtras(itemView to "collectionDetails")
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCollectionFragment(collectionId), extras)
        }, {
            viewModel.morePhotos()
        })
        with(binding.photosRecyclerView) {
            adapter = photosAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            binding.nameTextView.text = "${it.firstName} ${it.lastName}"
            binding.userNameTextView.text = "@${it.userName}"
            binding.bioTextView.text = it.bio
            if (it.location != null)
            binding.locationTextView.text = "${it.location.city}, ${it.location.country}"
            binding.locationImageView.setImageResource(R.drawable.ic_outline_location_on_24)
            binding.emailTextView.text = it.email
            binding.emailImageView.setImageResource(R.drawable.ic_outline_email_24)
            binding.downloadsTextView.text = it.downloads.toString()
            binding.downloadsImageView.setImageResource(R.drawable.ic_baseline_download_24)
            binding.photosTextView.text = getString(R.string.profilePhotos, it.totalPhotos)
            binding.likedPhotosTextView.text = getString(R.string.likedPhotos, it.totalLikes)
            binding.collectionsTextView.text = getString(R.string.profileCollections, it.totalCollections)
            binding.swipeRefreshLayout.isVisible = false
        }

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            Glide.with(binding.root)
                .load(it.avatar["medium"])
                .circleCrop()
                .into(binding.avatarImageView)
        }

        viewModel.profilePhotosLiveData.observe(viewLifecycleOwner) {
            if (viewModel.getListType() == 1) {
                binding.emptyListTextView.isVisible = it.isEmpty()
                binding.likedPhotosTextView.setTextColor(resources.getColor(R.color.translucent))
                binding.photosTextView.setTextColor(resources.getColor(R.color.black))
                binding.collectionsTextView.setTextColor(resources.getColor(R.color.translucent))
                if (binding.photosRecyclerView.adapter is CollectionsAdapter)
                  binding.photosRecyclerView.adapter = photosAdapter
                photosAdapter.updateList(it)
            }
        }

        viewModel.likedPhotosLiveData.observe(viewLifecycleOwner) {
            if (viewModel.getListType() == 2) {
                binding.emptyListTextView.isVisible = it.isEmpty()
                binding.likedPhotosTextView.setTextColor(resources.getColor(R.color.black))
                binding.photosTextView.setTextColor(resources.getColor(R.color.translucent))
                binding.collectionsTextView.setTextColor(resources.getColor(R.color.translucent))
                if (binding.photosRecyclerView.adapter is CollectionsAdapter)
                  binding.photosRecyclerView.adapter = photosAdapter
                photosAdapter.updateList(it)
            }
        }

        viewModel.profileCollectionsLiveData.observe(viewLifecycleOwner) {
            if (viewModel.getListType() == 3) {
                binding.emptyListTextView.isVisible = it.isEmpty()
                binding.likedPhotosTextView.setTextColor(resources.getColor(R.color.translucent))
                binding.photosTextView.setTextColor(resources.getColor(R.color.translucent))
                binding.collectionsTextView.setTextColor(resources.getColor(R.color.black))
                if (binding.photosRecyclerView.adapter is PhotosAdapter)
                  binding.photosRecyclerView.adapter = collectionsAdapter
                collectionsAdapter.updateList(it)
            }
        }

        viewModel.noConnectionLiveData.observe(viewLifecycleOwner){
            Snackbar.make(binding.root, getString(R.string.noConnection2), Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(requireContext(), R.color.translucent2))
                .setTextColor(getColor(requireContext(), R.color.red))
                .show()
        }

        viewModel.showMapLiveData.observe(viewLifecycleOwner) { position ->
            val latitude = position["latitude"]!!
            val longitude = position["longitude"]!!
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:$latitude,$longitude")
            }
            startActivity(intent)
        }

        viewModel.logoutLiveData.observe(viewLifecycleOwner){
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToStartFragment())
        }

        binding.photosTextView.setOnClickListener {
            viewModel.changeListType(1)
            viewModel.clearPage()
            viewModel.getProfilePhotos()
        }

        binding.likedPhotosTextView.setOnClickListener {
            viewModel.changeListType(2)
            viewModel.clearPage()
            viewModel.getLikedPhotos()
        }

        binding.collectionsTextView.setOnClickListener {
            viewModel.changeListType(3)
            viewModel.clearPage()
            viewModel.getProfileCollections()
        }

        binding.locationTextView.setOnClickListener {
            viewModel.showMap()
        }

        binding.materialToolbar.setOnMenuItemClickListener {
            LogoutDialog().show(childFragmentManager, null)
            true
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.clearPage()
            viewModel.changeListType(1)
            viewModel.getProfile()
            viewModel.getProfilePhotos()
        }
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    fun logout(){
      viewModel.logout()
    }
}