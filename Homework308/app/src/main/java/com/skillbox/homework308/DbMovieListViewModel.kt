package com.skillbox.homework308

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DbMovieListViewModel: ViewModel() {
    private val repository = Repository()
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies: StateFlow<List<Movie>> = _movies
    private var job: Job? = null

     fun observeMovies() {
           job = repository.observeMovies()
                    .onEach { _movies.value = it }
                    .launchIn(viewModelScope)
    }

     fun deleteMovie(movie: Movie){
         viewModelScope.launch { repository.deleteMovie(movie) }
    }

    fun cancelJob(){
        job?.cancel()
        job = null
    }
}