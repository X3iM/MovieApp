package com.android.hackathon.movieapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.request.MovieApiClient

object MovieRepository {

    private var movieApiClient = MovieApiClient

    fun getMovies(): LiveData<List<Movie>> = movieApiClient.getMovies()

    fun searchMovieApi(query: String, page: Int) {
        movieApiClient.searchMoviesApi(query, page)
    }

    fun getTrends(type: String, time: String, key: String) = movieApiClient.getTrends(type, time, key)

}