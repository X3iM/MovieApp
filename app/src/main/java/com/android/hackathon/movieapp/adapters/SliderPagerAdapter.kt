package com.android.hackathon.movieapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.models.Movie
import com.bumptech.glide.Glide

class SliderPagerAdapter(private var context: Context) : PagerAdapter() {

    var data: List<Movie>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        if (data != null) {
//            Log.v("Tag", "getItemCount ${data!!.size}")
            return data!!.size
        }
        return 0
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.slide_item, null)

        if (data != null) {
            val imageView = slideLayout.findViewById<ImageView>(R.id.slide_image)
            if (data!![position].poster_path != null) {
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/${data!![position].poster_path}")
                    .into(imageView)
            }
//            Log.v("Tag", "movie_id ${data!![position].id}")
            val text = slideLayout.findViewById<TextView>(R.id.slide_title)
            if (data!![position].title != null)
                text.text = data!![position].title
            else
                text.text = data!![position].name
        }

        container.addView(slideLayout)
        return slideLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}