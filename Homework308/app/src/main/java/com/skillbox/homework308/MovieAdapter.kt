package com.skillbox.homework308

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.skillbox.homework308.databinding.ItemMovieBinding

class MovieAdapter(private var delete: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MovieAdapter.Holder>() {

    private var movieList = emptyList<Movie>()

    fun update(newList: List<Movie>) {
        movieList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.item_movie), delete)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class Holder(view: View, private var delete: ((Int) -> Unit)?) : RecyclerView.ViewHolder(view){
        private val binding: ItemMovieBinding by viewBinding()
        init {
            if (delete != null)
                binding.deleteImageView.apply{
                    isVisible = true
                    setOnClickListener { delete!!(adapterPosition) }
                }

        }
        fun bind(movie: Movie) {
            binding.idTextView.text = "Идентификатор imdb: ${movie.id}"
            binding.titleTextView.text = movie.title
            binding.typeTextView.text = "Тип фильма: ${movie.type}"
            binding.yearTextView.text = "Год выпуска: ${movie.year}"
            Glide.with(binding.root)
                    .load(movie.poster)
                    .placeholder(R.drawable.ic_picture)
                    .error(R.drawable.ic_error)
                    .into(binding.posterImageView)
        }

    }
}