package com.skillbox.homework308

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.homework308.databinding.MovieListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieListFragment : Fragment(R.layout.movie_list) {
    private val viewModel: MovieListViewModel by viewModels()
    private val binding: MovieListBinding by viewBinding()
    private var movieAdapter: MovieAdapter? = MovieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dbMoviesButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.openDbMovieListFragment()
        }
        binding.moviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collect {
                movieAdapter?.update(it)
                movieAdapter?.notifyDataSetChanged()
            }
        }
        viewModel.bind(
                binding.searchEditText.textChangedFlow(),
                binding.radioGroup.checkChangeFlow()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelJob()
        movieAdapter = null
    }
}