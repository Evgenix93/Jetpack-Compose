package com.project.aperture.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.project.aperture.utils.DownloadWorker
import com.project.aperture.R
import com.project.aperture.databinding.PhotoFragmentBinding
import com.project.aperture.utils.haveQ

class PhotoFragment : Fragment(R.layout.photo_fragment) {
    private val args: PhotoFragmentArgs by navArgs()
    private val binding: PhotoFragmentBinding by viewBinding()
    private val viewModel: PhotoFragmentViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 700
        }
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it)
                    viewModel.launchWorkManager()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var firstLaunch = true

        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
            if (it == null) {
                binding.noConnectionTextView.isVisible = true
                binding.downloadsImageView.isVisible = false
                binding.locationTextView.isVisible = false
                binding.locationImageView.isVisible = false
                binding.loadingProgressBar.isVisible = false
                binding.photoImageView.isVisible = false
                binding.likeImageView.isVisible = false
            } else {
                binding.photoImageView.isVisible = true
                binding.noConnectionTextView.isVisible = false
                binding.downloadsImageView.isVisible = true
                binding.locationTextView.isVisible = true
                binding.locationImageView.isVisible = true
                Glide.with(binding.root)
                    .load(it.urls["raw"])
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.loadingProgressBar.isVisible = false
                            binding.sadImageView.isVisible = true
                            binding.noConnectionTextView.isVisible = true
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.sadImageView.isVisible = false
                            binding.noConnectionTextView.isVisible = false
                            binding.loadingProgressBar.isVisible = false
                            binding.photoImageView.setImageDrawable(resource)
                            return true
                        }
                    })
                    .into(binding.photoImageView)
                Glide.with(binding.root)
                    .load(it.user.avatar["small"])
                    .circleCrop()
                    .into(binding.avatarImageView)
                binding.nameTextView.text = it.user.name
                binding.userNameTextView.text = "@${it.user.userName}"
                binding.likesTextView.text = it.likes.toString()
                if (it.likedByUser)
                    binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
                else
                    binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                binding.locationTextView.text =
                    "${it.location?.city ?: "-"}, ${it.location?.country ?: "-"}"
                var tags = ""
                it.tags?.forEach { tag ->
                    tags += " #${tag.title}"
                }
                binding.tagsTextView.text = tags
                binding.exifTextView.text = """
               Made with:${it.exif?.make ?: "-"}
               Model:${it.exif?.model ?: "-"}
               Exposure:${it.exif?.exposureTime ?: "-"}
               Aperture:${it.exif?.aperture ?: "-"}
               Focal Length:${it.exif?.focalLength ?: "-"}
               ISO:${it.exif?.iso ?: "-"}
            """.trimIndent()
                binding.bioTextView.text = "About @${it.user.name}:\n${it.user.bio ?: "-"}"
                binding.downloadTextView.text = getString(R.string.download, it.downloads)
            }
        }

        viewModel.internetConnectionLiveData.observe(viewLifecycleOwner) {
            if (it.not())
                Snackbar.make(
                    binding.root,
                    getString(R.string.downloadError1),
                    Snackbar.LENGTH_LONG
                )
                    .setBackgroundTint(getColor(requireContext(), R.color.translucent2))
                    .setTextColor(getColor(requireContext(), R.color.red))
                    .show()
        }

        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DownloadWorker.DOWNLOAD_WORK_ID)
            .observe(viewLifecycleOwner) {
                if (firstLaunch) {
                    firstLaunch = false
                    return@observe
                }

                if (it.firstOrNull()?.state == WorkInfo.State.SUCCEEDED) {
                    viewModel.trackDownloads(args.id)
                    val imageUri =
                        Uri.parse(it.first().outputData.getString(DownloadWorker.IMAGE_URI)!!)
                    Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.downloadSuccess)) {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = imageUri
                            }
                            startActivity(intent)
                        }
                        .setBackgroundTint(getColor(requireContext(), R.color.translucent2))
                        .setActionTextColor(getColor(requireContext(), R.color.white))
                        .show()
                }
                if (it.firstOrNull()?.state == WorkInfo.State.ENQUEUED)
                    viewModel.checkInternet(args.id)
            }

        viewModel.showMapLiveData.observe(viewLifecycleOwner) { position ->
            val latitude = position["latitude"]!!
            val longitude = position["longitude"]!!
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:$latitude,$longitude")
            }
            startActivity(intent)
        }

        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.share) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, " https://unsplash.com/photos/${args.id}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, null)
                startActivity(shareIntent)
                true
            } else
                false
        }

        binding.downloadTextView.setOnClickListener {
            if (haveQ())
                viewModel.launchWorkManager()
            else
                requestPermissionLauncher.launch(PERMISSION)
        }

        binding.locationTextView.setOnClickListener {
            viewModel.showMap()
        }

        binding.likeImageView.setOnClickListener {
            viewModel.likePhoto()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPhoto(args.id)
        }

        viewModel.getPhoto(args.id)
    }

    companion object {
        private const val PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
}
