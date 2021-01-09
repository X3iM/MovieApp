package com.android.hackathon.movieapp.response

import com.android.hackathon.movieapp.models.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieSearchResponse {

    @SerializedName("total_results")
    @Expose
    private var totalCount: Int = 0

    @SerializedName("results")
    @Expose
    private lateinit var movieList: List<Movie>

    fun getTotalCount(): Int {
        return totalCount
    }

    fun getMovies(): List<Movie> {
        return movieList
    }

    override fun toString(): String {
        return "MovieSearchResponse(totalCount=$totalCount, movieList=$movieList)"
    }


}