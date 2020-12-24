package com.android.hackathon.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.models.Movie

class MovieAdapter(var context: Context, var data: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = Movie(data[position].title, data[position].description, data[position].thumbnail)
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView = itemView.findViewById(R.id.item_movie_image)
        private var image: ImageView = itemView.findViewById(R.id.item_movie_title)

        fun bind(movie: Movie) {
            title.text = movie.title
            image.setImageResource(movie.thumbnail)
        }

    }

}