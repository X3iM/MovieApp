package com.android.hackathon.movieapp.models

import android.net.Credentials
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.hackathon.movieapp.repositories.MovieRepository

class MovieListViewModel : ViewModel() {

    private var movieRepository: MovieRepository = MovieRepository()

    fun getMovies(): LiveData<List<Movie>> = movieRepository.getMovies()

    fun searchMovieApi(query: String, pageNum: Int) {
        movieRepository.searchMovieApi(query, pageNum)
    }

    fun getTrends(
        type: String = "all",
        time: String = "week",
        key: String = com.android.hackathon.movieapp.utils.Credentials.BASE_URL,
        page: Int = 1
    ) = movieRepository.getTrends(type, time, key, page)

    fun searchNextPage() {
        movieRepository.searchNextPage()
    }

    fun trendsNextPage() {
        movieRepository.trendsNextPage()
    }

}