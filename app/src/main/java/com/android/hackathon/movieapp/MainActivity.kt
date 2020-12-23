package com.android.hackathon.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewParent
import androidx.viewpager.widget.ViewPager
import com.android.hackathon.movieapp.adapters.SliderPagerAdapter
import com.android.hackathon.movieapp.models.Slide
import com.google.android.material.tabs.TabLayout
import java.util.*

class MainActivity : AppCompatActivity() {

    private val slideList = mutableListOf<Slide>()
    private lateinit var slidePager: ViewPager
    private lateinit var indicator: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        slidePager = findViewById(R.id.slider)
        indicator = findViewById(R.id.indicator)

        slideList.add(Slide(R.drawable.movie_preview_image_background, "Title"))
        slideList.add(Slide(R.drawable.movie_preview_image_background, "Title2"))

        var sliderPagerAdapter = SliderPagerAdapter(this, slideList)
        slidePager.adapter = sliderPagerAdapter

        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTask(), 4000, 6000)

        indicator.setupWithViewPager(slidePager, true)
    }

    inner class SliderTask : TimerTask() {

        override fun run() {
            runOnUiThread(Runnable {
                if (slidePager.currentItem < slideList.size - 1)
                    slidePager.currentItem = slidePager.currentItem + 1
                else
                    slidePager.currentItem = 0
            })
        }

    }

}