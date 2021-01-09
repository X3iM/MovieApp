package com.android.hackathon.movieapp.utils

import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.response.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("3/search/movie")
    fun searchMovie(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("page") page: String
    ): Call<MovieSearchResponse>

    @GET("3/movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: String, @Query("api_key") key: String): Call<Movie>

    @GET("/trending/{media_type}/{time_window}")
    fun getTrends(
        @Path("media_type") type: String = "all",
        @Path("time_window") time: String = "week",
        @Query("api_key") key: String = Credentials.API_KEY
    ): Call<Movie>

}