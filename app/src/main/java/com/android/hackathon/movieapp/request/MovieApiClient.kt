package com.android.hackathon.movieapp.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.hackathon.movieapp.AppExecutors
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.response.MovieSearchResponse
import com.android.hackathon.movieapp.utils.Credentials
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MovieApiClient {

    private var movies = MutableLiveData<List<Movie>>()
    private var retrieveMoviesRunnable: RetrieveMoviesRunnable? = null
    private var retrieveTrendsMovieRunnable: RetrieveTrendsMovieRunnable? = null

    fun getMovies(): LiveData<List<Movie>> = movies

    fun searchMoviesApi(query: String, pageNumber: Int) {
        if (retrieveMoviesRunnable != null) retrieveMoviesRunnable = null

        retrieveMoviesRunnable = RetrieveMoviesRunnable(query, pageNumber)

        val handler = AppExecutors.getNetworkIO().submit(retrieveMoviesRunnable)

        AppExecutors.getNetworkIO().schedule({
            handler.cancel(true)
        }, 5000, TimeUnit.MILLISECONDS)
    }

    fun getTrends(type: String, time: String, key: String, page: Int) {
        if (retrieveTrendsMovieRunnable != null) retrieveTrendsMovieRunnable = null

        retrieveTrendsMovieRunnable = RetrieveTrendsMovieRunnable(type, time, key, page)

        val handler = AppExecutors.getNetworkIO().submit(retrieveTrendsMovieRunnable)

        AppExecutors.getNetworkIO().schedule(Runnable {
            handler.cancel(true)
        }, 50000, TimeUnit.MILLISECONDS)
    }

    inner class RetrieveMoviesRunnable(var query: String, var pageNumber: Int) :
        Runnable {
        private var cancelRequest = false

        override fun run() {

            try {
                if (cancelRequest) return

                val response = getMovies(query, pageNumber).execute()
                if (response.code() == 200) {
                    val list = (response.body() as MovieSearchResponse).getMovies()
                    if (pageNumber == 1) {
                        movies.postValue(list)
                    } else {
                        val currentMovies: MutableList<Movie> = movies.value as MutableList<Movie>
                        currentMovies.addAll(list)
                        movies.postValue(currentMovies)
                    }
                } else {
                    Log.v("Tag", "Error: ${response.errorBody().toString()}")
                    movies.postValue(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                movies.postValue(null)
            }
        }

        private fun getMovies(query: String, pageNumber: Int): Call<MovieSearchResponse> {
            return Services.getMovieApi().searchMovie(
                key = Credentials.API_KEY,
                query = query,
                page = pageNumber.toString()
            )
        }

        private fun cancelRequest() {
            Log.v("Tag", "Cancel search request")
            cancelRequest = true
        }

    }

    inner class RetrieveTrendsMovieRunnable(var type: String, var time: String, var key: String, var pageNumber: Int) :
        Runnable {
        private var cancelRequest = false

        override fun run() {

            try {
                if (cancelRequest) return

                val response = getMovies(pageNumber).execute()
                if (response.code() == 200) {
                    val list = (response.body() as MovieSearchResponse).getMovies()
                    if (pageNumber == 1) {
                        movies.postValue(list)
                    } else {
                        val currentMovies: MutableList<Movie> = movies.value as MutableList<Movie>
                        currentMovies.addAll(list)
                        movies.postValue(currentMovies)
                    }
                } else {
                    Log.v("Tag", "Error on RetrieveTrendsMovieRunnable: ${response.errorBody().toString()}")
                    movies.postValue(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                movies.postValue(null)
            }
        }

        private fun getMovies(page: Int): Call<MovieSearchResponse> {
            return Services.getMovieApi().getTrends(
                page = page.toString()
            )
        }

        private fun cancelRequest() {
            Log.v("Tag", "Cancel search request")
            cancelRequest = true
        }

    }

}