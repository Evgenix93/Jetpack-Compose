package com.skillbox.homework308

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class MovieListViewModel: ViewModel() {
    private val repository = Repository()
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies:StateFlow<List<Movie>> = _movies
    private var job: Job? = null

    fun bind(queryFlow: Flow<String>, movieTypeFlow: Flow<MovieType>){
    job = combine(queryFlow, movieTypeFlow){query, movieType ->
        query to movieType
    }
        .debounce(1000)
        .mapLatest{(query, movieType) ->
            _movies.value = repository.searchMovies(query, movieType)
        }
        .launchIn(viewModelScope)
    }

    fun cancelJob(){
        job?.cancel()
        job = null
    }
}