package com.android.hackathon.movieapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.models.Movie
import com.bumptech.glide.Glide

class MovieAdapter(var onClickListener: OnMovieListener) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var data: List<Movie>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }

//        val movie = Movie(data[position].title, data[position].movie_overview, data[position].poster_path, data[position].vote_average)
//        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        if (data != null) {
//            Log.v("Tag", "getItemCount ${data!!.size}")
            return data!!.size
        }
        return 0
    }

    inner class ViewHolder(itemView: View, private var onClickListener: OnMovieListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

//        private var category: TextView = itemView.findViewById(R.id.main_movie_category)
//        private var duration: TextView = itemView.findViewById(R.id.main_movie_duration)
        private var title: TextView = itemView.findViewById(R.id.item_movie_title)
        private var image: ImageView = itemView.findViewById(R.id.item_movie_image)
        private var ratingBar: TextView = itemView.findViewById(R.id.item_movie_rate)

        fun bind(movie: Movie) {
            if (movie.name != null) title.text = movie.name
            else title.text = movie.title
            if (movie.poster_path != null)
                Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500/${movie.poster_path}").into(image)
//            Log.v("Tag", "movie runtime ${movie.runtime}")
//            duration.text = "${movie.runtime}"
//            category.text = movie.release_date
            ratingBar.text = "${movie.vote_average}"
//            Log.v("Tag", ratingBar.rating.toString())

//            val image = itemView.findViewById<ImageView>(R.id.movie_detail_image)
//            val name = itemView.findViewById<TextView>(R.id.movie_detail_movie_name)
            itemView.setOnClickListener(this)
//            itemView.setOnClickListener {
//                movieItemClickListener.onMovieClick(data[adapterPosition], Pair(image, "imageTransition"), Pair(title, "nameTransition"))
//            }
//            image.setImageResource(movie.thumbnail)
        }

        override fun onClick(v: View?) {
//            Log.v("Tag", "$adapterPosition")
            onClickListener.onMovieClick(adapterPosition)
        }

    }

}