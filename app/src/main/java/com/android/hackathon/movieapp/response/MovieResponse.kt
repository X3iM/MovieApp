package com.android.hackathon.movieapp.response

import com.android.hackathon.movieapp.models.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieResponse {

    @SerializedName("results")
    @Expose
    private lateinit var movie: Movie

    public fun getMovie(): Movie {
        return movie
    }

    override fun toString(): String {
        return "MovieResponse(movie=$movie)"
    }


}