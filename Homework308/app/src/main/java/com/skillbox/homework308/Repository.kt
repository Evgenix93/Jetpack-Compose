package com.skillbox.homework308


import kotlinx.coroutines.flow.Flow
import java.util.*



class Repository {
    private val moviesDao = Database.instance.moviesDao()

    suspend fun searchMovies(query: String, type: MovieType): List<Movie>{
        return try {
            val movies = Network.api.searchMovies(Network.API_KEY, query, type.name).movies ?: emptyList()
            val moviesBD = moviesDao.getMovies()
            val newMovies = movies - moviesBD
            moviesDao.saveMovies(newMovies.toSet().toList())
            movies
        }catch (t: Throwable){
            if (query.isBlank())
                return emptyList()
            moviesDao.getMovies().filter {
                it.title.contains(query, true) && it.type == type.name.toLowerCase(Locale.getDefault())
            }
        }
 }

     fun observeMovies(): Flow<List<Movie>> {
        return moviesDao.observeMovies()
    }

    suspend fun deleteMovie(movie: Movie){
        moviesDao.deleteMovie(movie)
    }
}