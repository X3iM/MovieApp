package com.android.hackathon.movieapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hackathon.movieapp.MainActivity
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.adapters.MovieAdapter
import com.android.hackathon.movieapp.adapters.OnMovieListener
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.models.MovieSavedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteActivity : AppCompatActivity(), OnMovieListener {

    private val bottomNavBar by lazy { findViewById<BottomNavigationView>(R.id.bottom_nav_bar) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.fav_rec_view) }

    private val text by lazy { findViewById<TextView>(R.id.fav_text) }
    private val textEmpty by lazy { findViewById<TextView>(R.id.fav_db_empty_text) }

    private val viewModel: MovieSavedViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(application) }

    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        adapter = MovieAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        text.visibility = View.GONE
        textEmpty.visibility = View.VISIBLE
        observeAnyChange()

        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.nav_person -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
            }
            true
        }
    }

    private fun observeAnyChange() {
        viewModel.getAllSavedMovie().observe(this, {
            val data = mutableListOf<List<Movie>>(it)
            Log.d("Tag", "observeAnyChange ${data}")
            if (data.isNotEmpty()) {
                text.visibility = View.VISIBLE
                textEmpty.visibility = View.GONE
                adapter.data = data[0]
            } else {
                text.visibility = View.GONE
                textEmpty.visibility = View.VISIBLE
            }
        })
    }

    override fun onMovieClick(position: Int) {
        if (viewModel.getAllSavedMovie().value != null) {
            val list = viewModel.getAllSavedMovie().value
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("id", list?.get(position)?.id)
            intent.putExtra("title", list?.get(position)?.title)
            intent.putExtra("vote_average", list?.get(position)?.vote_average)
            intent.putExtra("overview", list?.get(position)?.overview)
            intent.putExtra("poster_path", list?.get(position)?.poster_path)
            intent.putExtra("name", list?.get(position)?.name)
            intent.putExtra("runtime", list?.get(position)?.runtime)
            intent.putExtra("release_date", list?.get(position)?.release_date)
            startActivity(intent)
        }
    }

    override fun onCategoryClick(category: String) {
        TODO("Not yet implemented")
    }

}
