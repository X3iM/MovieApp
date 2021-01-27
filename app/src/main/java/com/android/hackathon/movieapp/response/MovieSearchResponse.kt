package com.android.hackathon.movieapp.response

import com.android.hackathon.movieapp.models.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieSearchResponse {

    @SerializedName("total_pages")
    @Expose
    private var totalPages: Int = 0

    @SerializedName("results")
    @Expose
    private lateinit var movieList: List<Movie>

    fun getTotalPages(): Int {
        return totalPages
    }

    fun getMovies(): List<Movie> {
        return movieList
    }

    override fun toString(): String {
        return "MovieSearchResponse(totalCount=$totalPages, movieList=$movieList)"
    }


}