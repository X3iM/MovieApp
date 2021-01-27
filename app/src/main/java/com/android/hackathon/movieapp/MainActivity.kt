package com.android.hackathon.movieapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.hackathon.movieapp.activities.FavoriteActivity
import com.android.hackathon.movieapp.activities.MovieDetailActivity
import com.android.hackathon.movieapp.activities.ProfileActivity
import com.android.hackathon.movieapp.adapters.MovieAdapter
import com.android.hackathon.movieapp.adapters.OnMovieListener
import com.android.hackathon.movieapp.adapters.SliderPagerAdapter
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.models.MovieListViewModel
import com.android.hackathon.movieapp.models.Slide
import com.android.hackathon.movieapp.request.Services
import com.android.hackathon.movieapp.response.MovieSearchResponse
import com.android.hackathon.movieapp.utils.Credentials
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), OnMovieListener {

    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var trendsListViewModel: MovieListViewModel

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderPagerAdapter: SliderPagerAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var slidePager: ViewPager
    private lateinit var timer: Timer

    private lateinit var bottomNavBar: BottomNavigationView

    private val slideList = mutableListOf<Slide>()
    private val movieList = mutableListOf<Movie>()

    private lateinit var indicator: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
        setContentView(R.layout.activity_main)

        slidePager = findViewById(R.id.slider)

        movieAdapter = MovieAdapter(this)
        val movie_recycler_view = findViewById<RecyclerView>(R.id.movie_recycler_view)
        movie_recycler_view.adapter = movieAdapter
        movie_recycler_view.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

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

        sliderPagerAdapter = SliderPagerAdapter(this)
        slidePager.adapter = sliderPagerAdapter
        observeAnyChange()
//        val profileFragment = ProfileFragment()
//        val homeFragment = HomeFragment()

        bottomNavBar = findViewById(R.id.bottom_nav_bar)
        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_fav -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    finish()
                }
                R.id.nav_person -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
            }
            true
        }


//        val toolbar = findViewById<Toolbar>(R.id.main_tool_bar)
//        setSupportActionBar(toolbar)

//        recyclerView = findViewById(R.id.main_recycler_view)


//        prepareRecView()

//        searchViewSetup()
//        movieListViewModel.getTrends()

//        movieAdapter.data = movieListViewModel.getMovies().value
//        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (!recyclerView.canScrollVertically(1)) {
//                    movieListViewModel.trendsNextPage()
//                }
//            }
//        })


//        sliderPagerAdapter.data = movieListViewModel.getMovies().value
//
        timer = Timer()
        timer.scheduleAtFixedRate(SliderTask(), 6000, 6000)
//
//        indicator.setupWithViewPager(slidePager, true)

    }

//    private fun searchViewSetup() {
//        val searchView: androidx.appcompat.widget.SearchView = findViewById(R.id.search_view)
//
//        searchView.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                movieListViewModel.searchMovieApi(query, 1)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//    }

//    private fun searchMovieApi(query: String, pageNum: Int) {
//        movieListViewModel.searchMovieApi(query, pageNum)
//    }

    private fun getRetrofitResponse() {
        val movieApi = Services.getMovieApi()

        var responseCall = movieApi.searchMovie(
            key = Credentials.API_KEY,
            query = "Jack",
            page = "1"
        )

        responseCall.enqueue(object : Callback<MovieSearchResponse> {

            override fun onResponse(
                call: Call<MovieSearchResponse>,
                response: Response<MovieSearchResponse>
            ) {
                if (response.code() == 200) {
//                    Log.v("Tag", "The response ${response.body().toString()}")
                    val movies = response.body()?.getMovies()

                    movies?.forEach {
//                        Log.v("Tag", "Movie id ${it.movie_id}")
                    }
                } else {
                    try {
//                        Log.v("Tag", "Error ${response.body().toString()}")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getRetrofitResponseAccordingToID() {
//        val movieApi = Services.getMovieApi()
//
//        var responseCall = movieApi.getMovie(
//            movieId = ,
//            query = "Jack",
//            page = "1"
//        )
    }

    inner class SliderTask : TimerTask() {

        override fun run() {
            runOnUiThread {
                if (trendsListViewModel.getMovies().value != null) {
                    if (slidePager.currentItem < trendsListViewModel.getMovies().value!!.size - 1)
                        slidePager.currentItem = slidePager.currentItem + 1
                    else
                        slidePager.currentItem = 0
                }
            }
        }

    }


    override fun onPause() {
        super.onPause()
//        onLeaveThisActivity()
    }

//    override fun onMovieClick(movie: Movie, vararg sharedElements: Pair<View, String>) {
//        val intent = Intent(this, MovieDetailActivity::class.java)
//        intent.putExtra("title", movie.title)
//        intent.putExtra("trailerURI", movie.vote_average)
//        intent.putExtra("description", movie.movie_overview)
//        intent.putExtra("thumbnail", movie.poster_path)
//
//        val activityOption = ActivityOptions.makeSceneTransitionAnimation(this, *sharedElements)
//
//        startActivity(intent, activityOption.toBundle())
////        Toast.makeText(this, movie.title, Toast.LENGTH_LONG).show()
//    }

    private fun onLeaveThisActivity() {
        print("change anim")
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

//    private fun prepareRecView() {
//        movieAdapter = MovieAdapter(this)
//
//        recyclerView.adapter = movieAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//    }

    private fun observeAnyChange() {
        movieListViewModel.getMovies().observe(this, androidx.lifecycle.Observer { movieModels ->
            val newList = mutableListOf<List<Movie>>(movieModels)
            newList.forEach { _ ->
                movieAdapter.data = movieModels
            }
        })
        trendsListViewModel.getMovies().observe(this, androidx.lifecycle.Observer { movieModels ->
            movieModels?.forEach { _ ->
                sliderPagerAdapter.data = movieModels
            }
        })
    }

    override fun onMovieClick(position: Int) {
        if (movieListViewModel.getMovies().value != null) {
            val list = movieListViewModel.getMovies().value
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
//
//        val activityOption = ActivityOptions.makeSceneTransitionAnimation(this, *sharedElements)
//
//        startActivity(intent, activityOption.toBundle())
        }
    }

    override fun onCategoryClick(category: String) {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        super.onStop()

        timer.cancel()
        viewModelStore.clear()
    }

}