package com.android.hackathon.movieapp.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.adapters.MovieAdapter
import com.android.hackathon.movieapp.adapters.OnMovieListener
import com.android.hackathon.movieapp.adapters.SliderPagerAdapter
import com.android.hackathon.movieapp.models.MovieListViewModel

class HomeFragment : Fragment(), OnMovieListener {

    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var trendsListViewModel: MovieListViewModel

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderPagerAdapter: SliderPagerAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var slidePager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            slidePager = container.findViewById(R.id.slider)

            movieAdapter = MovieAdapter(this)
            val movie_recycler_view = container.findViewById<RecyclerView>(R.id.movie_recycler_view)
            movie_recycler_view.adapter = movieAdapter
            movie_recycler_view.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            trendsListViewModel = MovieListViewModel()
            movieListViewModel = MovieListViewModel()
            movieListViewModel.searchMovieApi("fast", 1)
            trendsListViewModel.getTrends()
            slidePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
//                Log.v("Tag", "onPageScrolled: position - $position, positionOffset - $positionOffset, positionOffsetPixels - $positionOffsetPixels")
                }

                override fun onPageSelected(position: Int) {
//                Log.v("Tag", "onPageSelected: position - $position")
                    if (position % 19 == 0)
                        trendsListViewModel.trendsNextPage()
                }

                override fun onPageScrollStateChanged(state: Int) {
//                Log.v("Tag", "onPageScrollStateChanged: state - $state")
                }
            })

            movie_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        movieListViewModel.searchNextPage()
                    }
                }
            })

            sliderPagerAdapter = context?.let { SliderPagerAdapter(it) }!!
            slidePager.adapter = sliderPagerAdapter
            observeAnyChange()
        }
        return null
//        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun observeAnyChange() {
        movieListViewModel.getMovies().observe(viewLifecycleOwner, androidx.lifecycle.Observer { movieModels ->
            movieModels?.forEach { _ ->
//                Log.v("Tag", "onChange: ${it.title}")
                movieAdapter.data = movieModels
            }
        })
        trendsListViewModel.getMovies().observe(viewLifecycleOwner, androidx.lifecycle.Observer { movieModels ->
            movieModels?.forEach { _ ->
                sliderPagerAdapter.data = movieModels
            }
        })
    }

    override fun onMovieClick(position: Int) {
        Toast.makeText(
            context,
            movieListViewModel.getMovies().value?.get(position)?.name,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCategoryClick(category: String) {
        TODO("Not yet implemented")
    }
}