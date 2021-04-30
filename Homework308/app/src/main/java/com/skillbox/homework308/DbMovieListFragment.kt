package com.skillbox.homework308

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.homework308.databinding.DbMovieListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DbMovieListFragment: Fragment(R.layout.db_movie_list) {
    private var movieAdapter: MovieAdapter? = MovieAdapter{viewModel.deleteMovie(viewModel.movies.value[it])}
    private val viewModel: DbMovieListViewModel by viewModels()
    private val binding: DbMovieListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dbMoviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.observeMovies()
            viewModel.movies.collect {
                movieAdapter?.update(it)
                movieAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelJob()
        movieAdapter = null
    }
}