package com.android.hackathon.movieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import com.android.hackathon.movieapp.R
import com.bumptech.glide.Glide
import com.sdsmdg.harjot.longshadows.LongShadowsImageView

class MovieDetailActivity : AppCompatActivity() {

    private val _movieImage by lazy { findViewById<LongShadowsImageView>(R.id.detail_movie_image) }
    private val _movieTrailer by lazy { findViewById<VideoView>(R.id.movie_detail_trailer) }
    private val _movieTitle by lazy { findViewById<TextView>(R.id.detail_movie_title) }
    private val _movieDesc by lazy { findViewById<TextView>(R.id.detail_movie_description) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        if (intent != null) {
            _movieTitle.text = intent.getStringExtra("title")
            _movieDesc.text = intent.getStringExtra("description")
            _movieTrailer.setVideoPath(intent.getStringExtra("trailerURI"))
            print(_movieTrailer)
//            Toast.makeText(this, "${_movieTrailer.toString()}", Toast.LENGTH_LONG).show()
            _movieTrailer.start()
            Glide.with(this).load(intent.getStringExtra("thumbnail")).into(_movieImage)
        }
    }

}