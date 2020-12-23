package com.android.hackathon.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.models.Slide

class SliderPagerAdapter(private var context: Context, private var list : List<Slide>) : PagerAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.slide_item, null)

        val imageView = slideLayout.findViewById<ImageView>(R.id.slide_image)
        imageView.setImageResource(list[position].image)
        val text = slideLayout.findViewById<TextView>(R.id.slide_title)
        text.text = list[position].title

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