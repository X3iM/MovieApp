package com.android.hackathon.movieapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.hackathon.movieapp.MainActivity
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.models.MovieSavedViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var bottomNavBar: BottomNavigationView

    private val _movieImage by lazy { findViewById<ImageView>(R.id.detail_movie_image) }
    private val _movieTrailer by lazy { findViewById<ImageView>(R.id.movie_detail_trailer) }
    private val _movieTitle by lazy { findViewById<TextView>(R.id.detail_movie_title) }
    private val _movieDesc by lazy { findViewById<TextView>(R.id.detail_movie_description) }
    private val _rate by lazy { findViewById<TextView>(R.id.detail_rate) }
    private val _addMovie by lazy { findViewById<FloatingActionButton>(R.id.detail_add_movie_button) }

    private lateinit var curMovie: Movie

    private val movieModel: MovieSavedViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        if (intent != null) {
            if (intent.getStringExtra("title") != null)
                _movieTitle.text = intent.getStringExtra("title")
            else
                _movieTitle.text = intent.getStringExtra("name")
            _movieDesc.text = intent.getStringExtra("overview")
            _rate.text = "${intent.getFloatExtra("vote_average", 0.0F)}"
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/${intent.getStringExtra("poster_path")}")
                .into(_movieImage)
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/${intent.getStringExtra("poster_path")}")
                .into(_movieTrailer)

//            val factory = MovieSavedVieModel.Factory(application)
//            movieModel = ViewModelProvider(this).get(MovieSavedViewModel::class.java)

            curMovie = Movie(
                id = intent.getIntExtra("id", 0),
                title = intent.getStringExtra("title"),
                vote_average = intent.getFloatExtra("vote_average", 0.0F),
                overview = intent.getStringExtra("overview"),
                poster_path = intent.getStringExtra("poster_path"),
                name = intent.getStringExtra("name"),
                runtime = intent.getIntExtra("runtime", 0),
                release_date = intent.getStringExtra("release_date"),
            )

        }

        bottomNavBar = findViewById(R.id.bottom_nav_bar)
        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_person -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_fav -> startActivity(Intent(this, FavoriteActivity::class.java))
            }
            true
        }

        _addMovie.setOnClickListener {
            movieModel.addUser(curMovie)
            Log.v("Tag", "insert")
        }
    }
}
