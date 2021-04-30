package com.skillbox.homework308

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MoviesDao {

    @Query ("SELECT * FROM ${MovieContract.TABLE_NAME}")
   suspend fun getMovies(): List<Movie>

    @Query ("SELECT * FROM ${MovieContract.TABLE_NAME}")
    fun observeMovies(): Flow<List<Movie>>

   @Insert
   suspend fun saveMovies(movies: List<Movie>)

   @Delete
   suspend fun deleteMovie(movie: Movie)
}