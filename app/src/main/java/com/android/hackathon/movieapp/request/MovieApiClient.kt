package com.android.hackathon.movieapp.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.hackathon.movieapp.AppExecutors
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.repositories.MovieRepository
import com.android.hackathon.movieapp.response.MovieSearchResponse
import com.android.hackathon.movieapp.utils.Credentials
import retrofit2.Call
import java.lang.Exception
import java.util.concurrent.TimeUnit

object MovieApiClient {

    private var movies = MutableLiveData<List<Movie>>()
    private var retrieveMoviesRunnable: RetrieveMoviesRunnable? = null

    fun getMovies(): LiveData<List<Movie>> = movies

    fun searchMoviesApi(query: String, pageNumber: Int) {
        if (retrieveMoviesRunnable != null) retrieveMoviesRunnable = null

        retrieveMoviesRunnable = RetrieveMoviesRunnable(query, pageNumber)

        val handler = AppExecutors.getNetworkIO().submit(retrieveMoviesRunnable)

        AppExecutors.getNetworkIO().schedule(Runnable {
            handler.cancel(true)
        }, 5000, TimeUnit.MILLISECONDS)
    }

    fun getTrends(type: String, time: String, key: String) {
        if (retrieveMoviesRunnable != null) retrieveMoviesRunnable = null

        retrieveMoviesRunnable = RetrieveMoviesRunnable(query, pageNumber)

        val handler = AppExecutors.getNetworkIO().submit(retrieveMoviesRunnable)

        AppExecutors.getNetworkIO().schedule(Runnable {
            handler.cancel(true)
        }, 5000, TimeUnit.MILLISECONDS)
    }

    class RetrieveMoviesRunnable(private var query: String, private var pageNumber: Int) :
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

}