package com.project.aperture.ui

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.project.aperture.R
import com.project.aperture.data.Photo
import com.project.aperture.data.PhotoDB
import com.project.aperture.databinding.ButtonItemBinding
import com.project.aperture.databinding.PhotoItemBinding
import com.project.aperture.utils.inflate
import java.io.File

class PhotosAdapter(
    private val collectionPhotos: Boolean,
    private val onClick: (String, View) -> Unit,
    private val onLike: (Int, Boolean) -> Unit,
    private val onButtonClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var photos = mutableListOf<Photo>()
    private var photosDB = emptyList<PhotoDB>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PHOTO_TYPE)
            PhotoViewHolder(collectionPhotos, parent.inflate(R.layout.photo_item), onClick, onLike)
        else
            ButtonViewHolder(parent.inflate(R.layout.button_item), onButtonClick)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            BUTTON_TYPE
        else
            PHOTO_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                if (photos.isNotEmpty())
                    holder.bind(photos[position])
                else
                    holder.bind(photosDB[position])
            }
            is ButtonViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (photos.isEmpty() && photosDB.isEmpty())
            0
        else if(photos.isEmpty())
            photosDB.size + 1
        else
            photos.size + 1
    }

    fun updateList(newList: List<Photo>) {
        photos = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun updateListDB(newList: List<PhotoDB>) {
        photosDB = newList
        notifyDataSetChanged()
    }

    fun updatePhoto(photo: Photo, position: Int) {
        photos[position] = photo
        notifyItemChanged(position)
    }

    companion object {
        private const val PHOTO_TYPE = 1
        private const val BUTTON_TYPE = 2
    }

    class PhotoViewHolder(
        private val collectionPhotos: Boolean,
        view: View,
        private val onClick: (String, View) -> Unit,
        private val onLike: (Int, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val binding: PhotoItemBinding by viewBinding()

        fun bind(photo: Photo) {
            itemView.transitionName = "photoItem${photo.id}"
            if (collectionPhotos) {
                binding.photoImageView.layoutParams.height = 1000
                binding.photoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.photoImageView.adjustViewBounds = false
            }else if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                val layoutParams =
                    itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = adapterPosition == 0
            }
            itemView.setOnClickListener {
                onClick(photo.id, binding.root)
            }
            binding.likesTextView.text = photo.likes.toString()
            binding.nameTextView.text = photo.user.name
            binding.userNameTextView.text = "@${photo.user.userName}"
            binding.likeImageView.setOnClickListener {
                if (photo.likedByUser)
                    onLike(adapterPosition, true)
                else
                    onLike(adapterPosition, false)
            }
            if (photo.likedByUser)
                binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
            else
                binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)

            Glide.with(itemView)
                .load(photo.urls["raw"])
                .addListener(object : RequestListener<Drawable> {
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
                        binding.photoImageView.setImageDrawable(resource)
                        binding.sadImageView.isVisible = false
                        binding.errorTextView.isVisible = false
                        binding.loadingProgressBar.isVisible = false
                        binding.photoImageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        return true
                    }
                })
                .into(binding.photoImageView)

            Glide.with(binding.root)
                .load(photo.user.avatar["small"])
                .circleCrop()
                .into(binding.avatarImageView)
        }

        fun bind(photoDB: PhotoDB) {
            itemView.transitionName = "photoItem${photoDB.photoId}"
            if (collectionPhotos) {
                binding.photoImageView.layoutParams.height = 1000
                binding.photoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.photoImageView.adjustViewBounds = false
            }else if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                val layoutParams =
                    itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = adapterPosition == 0
            }
            itemView.setOnClickListener {
                onClick(photoDB.photoId, itemView)
            }
            binding.likesTextView.text = photoDB.likes.toString()
            binding.nameTextView.text = photoDB.name
            binding.userNameTextView.text = "@${photoDB.userName}"
            Glide.with(itemView)
                .load(File(photoDB.photoPath))
                .addListener(object : RequestListener<Drawable> {
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
                        binding.photoImageView.setImageDrawable(resource)
                        binding.loadingProgressBar.isVisible = false
                        binding.sadImageView.isVisible = false
                        binding.errorTextView.isVisible = false
                        binding.photoImageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        return true
                    }
                })
                .into(binding.photoImageView)

            Glide.with(itemView)
                .load(photoDB.avatar)
                .circleCrop()
                .into(binding.avatarImageView)

            if (photoDB.likedByUser == 1) {
                binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else
                binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    class ButtonViewHolder(view: View, private val onButtonClick: () -> Unit) :
        RecyclerView.ViewHolder(view) {
        init {
            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                val layoutParams = itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
            }
        }
        private val binding: ButtonItemBinding by viewBinding()

        fun bind() {
            binding.moreButton.setOnClickListener {
                onButtonClick()
            }
        }
    }
}