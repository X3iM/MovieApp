package com.android.hackathon.movieapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.hackathon.movieapp.repositories.MovieRepository

class MovieListViewModel : ViewModel() {

    private var movieRepository: MovieRepository = MovieRepository

    fun getMovies(): LiveData<List<Movie>> = movieRepository.getMovies()

    fun searchMovieApi(query: String, pageNum: Int) {
        movieRepository.searchMovieApi(query, pageNum)
    }

    fun getTrends(type: String, time: String, key: String) = movieRepository.getTrends(type, time, key)

}