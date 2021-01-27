package com.android.hackathon.movieapp.models

data class User(
    var id: String,
    var name: String,
    var email: String,
    var image: String = "default",
    var balance: String = "$0.0"
)
