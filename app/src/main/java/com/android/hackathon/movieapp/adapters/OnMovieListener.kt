package com.android.hackathon.movieapp.adapters

interface OnMovieListener {

    fun onMovieClick(position: Int)
    fun onCategoryClick(category: String)

}