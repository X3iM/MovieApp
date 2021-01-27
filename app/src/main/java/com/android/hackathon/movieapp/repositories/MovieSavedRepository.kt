package com.android.hackathon.movieapp.repositories

import androidx.lifecycle.LiveData
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.room.MovieDao

class MovieSavedRepository(private val movieDao: MovieDao) {

    val readAllData: LiveData<List<Movie>> = movieDao.getSavedMovies()

    suspend fun addMovie(movie: Movie) {
        movieDao.addMovie(movie)
    }

}