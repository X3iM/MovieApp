package com.android.hackathon.movieapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.request.MovieApiClient

class MovieRepository {

    private var movieApiClient = MovieApiClient()

    private lateinit var query: String
    private var currentPage = 0

    fun getMovies(): LiveData<List<Movie>> = movieApiClient.getMovies()

    fun searchMovieApi(query: String, page: Int) {
        this.query = query
        this.currentPage = page
        movieApiClient.searchMoviesApi(query, page)
    }

    fun getTrends(
        type: String = "all",
        time: String = "week",
        key: String = com.android.hackathon.movieapp.utils.Credentials.BASE_URL,
        page: Int = 1
    ) {
        this.currentPage = page
        return movieApiClient.getTrends(type, time, key, page)
    }

    fun searchNextPage() {
        searchMovieApi(query, currentPage+1)
    }

    fun trendsNextPage() {
        getTrends(page = currentPage+1)
    }

}