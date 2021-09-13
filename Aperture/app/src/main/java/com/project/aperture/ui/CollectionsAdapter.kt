package com.project.aperture.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.project.aperture.R
import com.project.aperture.data.Collection
import com.project.aperture.data.CollectionDB
import com.project.aperture.databinding.ButtonItemBinding
import com.project.aperture.databinding.CollectionItemBinding
import com.project.aperture.utils.inflate
import java.io.File

class CollectionsAdapter(private val context: Context, private val onClick: (String, View) -> Unit, private val onButtonClick: () -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var collections = emptyList<Collection>()
    private var collectionsDB = emptyList<CollectionDB>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PHOTO_TYPE)
            CollectionViewHolder(
                context,
                parent.inflate(R.layout.collection_item),
                onClick
            )
        else
            ButtonViewHolder(
                parent.inflate(R.layout.button_item),
                onButtonClick
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CollectionViewHolder -> {
                if (collections.isNotEmpty())
                    holder.bind(collections[position])
                else
                    holder.bind(collectionsDB[position])
            }
            is ButtonViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (collections.isEmpty() && collectionsDB.isEmpty())
            0
        else if(collections.isEmpty())
            collectionsDB.size + 1
        else
            collections.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            BUTTON_TYPE
        else
            PHOTO_TYPE
    }

    fun updateList(newList: List<Collection>) {
        collections = newList
        notifyDataSetChanged()
    }

    fun updateListDB(newList: List<CollectionDB>) {
        collectionsDB = newList
        notifyDataSetChanged()
    }

    class CollectionViewHolder(private val context: Context, view: View, private val onClick: (String, View) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding: CollectionItemBinding by viewBinding()

        fun bind(collection: Collection) {
            itemView.transitionName = "collection${collection.id}"
            itemView.setOnClickListener {
                onClick(collection.id, itemView)
            }
            binding.photoCount.text = context.getString(R.string.photoCount, collection.totalPhotos)
            binding.titleTextView.text = collection.title
            binding.nameTextView.text = collection.user.name
            binding.collectionUserNameTextView.text = "@${collection.user.userName}"
            Glide.with(binding.root)
                .load(collection.coverPhoto?.urls?.get("raw"))
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
                        binding.coverPhoto.setImageDrawable(resource)
                        binding.loadingProgressBar.isVisible = false
                        binding.sadImageView.isVisible = false
                        binding.errorTextView.isVisible = false
                        return true
                    }
                })
                .into(binding.coverPhoto)
            Glide.with(itemView)
                .load(collection.user.avatar["small"])
                .circleCrop()
                .into(binding.avatarImageView)
        }

        fun bind(collectionDB: CollectionDB) {
            itemView.transitionName = "collection${collectionDB.collectionId}"
            itemView.setOnClickListener {
                onClick(collectionDB.collectionId, itemView)
            }
            binding.photoCount.text = context.getString(R.string.photoCount, collectionDB.totalPhotos)
            binding.titleTextView.text = collectionDB.title
            binding.nameTextView.text = collectionDB.name
            binding.collectionUserNameTextView.text = "@${collectionDB.userName}"
            Glide.with(itemView)
                .load(File(collectionDB.coverPhotoPath))
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
                        binding.coverPhoto.setImageDrawable(resource)
                        binding.loadingProgressBar.isVisible = false
                        binding.sadImageView.isVisible = false
                        binding.errorTextView.isVisible = false
                        return true
                    }
                })
                .into(binding.coverPhoto)
            Glide.with(itemView)
                .load(collectionDB.avatarByteArray)
                .circleCrop()
                .into(binding.avatarImageView)
        }
    }

    class ButtonViewHolder(view: View, private val onButtonClick: () -> Unit):
        RecyclerView.ViewHolder(view) {
        private val binding: ButtonItemBinding by viewBinding()

        fun bind() {
            binding.moreButton.setOnClickListener {
                onButtonClick()
            }
        }
    }

    companion object {
        private const val PHOTO_TYPE = 1
        private const val BUTTON_TYPE = 2
    }
}