package com.android.hackathon.movieapp.request

import com.android.hackathon.movieapp.utils.Credentials
import com.android.hackathon.movieapp.utils.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Services {

    private val retrofitBuilder =
        Retrofit.Builder().baseUrl(Credentials.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        )

    private val retrofit = retrofitBuilder.build()
    private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)

    fun getMovieApi(): MovieApi {
        return movieApi
    }
}