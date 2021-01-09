package com.android.hackathon.movieapp.adapters
import android.util.Pair
import android.view.View
import com.android.hackathon.movieapp.models.Movie

interface MovieItemClickListener {
    fun onMovieClick(movie: Movie, vararg sharedElements: Pair<View, String>)
}