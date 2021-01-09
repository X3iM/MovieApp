package com.android.hackathon.movieapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hackathon.movieapp.adapters.MovieAdapter
import com.android.hackathon.movieapp.adapters.OnMovieListener
import com.android.hackathon.movieapp.models.Movie
import com.android.hackathon.movieapp.models.MovieListViewModel
import com.android.hackathon.movieapp.models.Slide
import com.android.hackathon.movieapp.request.Services
import com.android.hackathon.movieapp.response.MovieSearchResponse
import com.android.hackathon.movieapp.utils.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity(), OnMovieListener {

    private lateinit var movieListViewModel: MovieListViewModel

    private lateinit var movieAdapter: MovieAdapter

    private lateinit var recyclerView: RecyclerView
    private val slideList = mutableListOf<Slide>()
    private val movieList = mutableListOf<Movie>()

//    private lateinit var slidePager: ViewPager
//    private lateinit var indicator: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//        slidePager = findViewById(R.id.slider)
//        indicator = findViewById(R.id.indicator)

        val toolbar = findViewById<Toolbar>(R.id.main_tool_bar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.main_recycler_view)

        movieListViewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)

        prepareRecView()
        observeAnyChange()

        searchMovieApi("Fast", 1)

//        getRetrofitResponse()

//        slideList.add(Slide("https://upload.wikimedia.org/wikipedia/ru/7/72/Fantasy_Island_%28film%2C_2020%29.jpg", "Остров фантазий"))
//        slideList.add(Slide("https://upload.wikimedia.org/wikipedia/ru/b/b9/The_Christmas_Chronicles_%28film%2C_2018%29.jpg", "Рождественские хроники"))
//        slideList.add(Slide("https://upload.wikimedia.org/wikipedia/ru/9/9d/Bloodshot_poster.jpg", "Бладшот"))
//        slideList.add(Slide("https://upload.wikimedia.org/wikipedia/ru/e/e1/The_Old_Guard_%282020_film%29.jpg", "Бессмертная гвардия"))
//
//        movieList.add(Movie("Колдовство: Новый ритуал", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fermentum massa sem. Maecenas tempus pretium accumsan. Maecenas sit amet accumsan neque, quis euismod massa. Donec mi est, viverra sit amet sollicitudin vitae, volutpat ac odio. Etiam et semper eros, quis malesuada nisi. Mauris gravida turpis a dolor rutrum congue. Sed vestibulum enim eget lectus lacinia, in scelerisque magna aliquam.", "https://s.kinokrad.co/uploads/img/6e298da7763129fe1d8e73c9862b6978.jpeg", "https://youtu.be/-6jCNhebfg8"))
//        movieList.add(Movie("Платформа", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fermentum massa sem. Maecenas tempus pretium accumsan. Maecenas sit amet accumsan neque, quis euismod massa. Donec mi est, viverra sit amet sollicitudin vitae, volutpat ac odio. Etiam et semper eros, quis malesuada nisi. Mauris gravida turpis a dolor rutrum congue. Sed vestibulum enim eget lectus lacinia, in scelerisque magna aliquam.", "https://upload.wikimedia.org/wikipedia/ru/7/72/El_hoyo_%282019%29.jpg", "https://youtu.be/-6jCNhebfg8"))
//        movieList.add(Movie("Шестеро вне закона", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fermentum massa sem. Maecenas tempus pretium accumsan. Maecenas sit amet accumsan neque, quis euismod massa. Donec mi est, viverra sit amet sollicitudin vitae, volutpat ac odio. Etiam et semper eros, quis malesuada nisi. Mauris gravida turpis a dolor rutrum congue. Sed vestibulum enim eget lectus lacinia, in scelerisque magna aliquam.", "https://thumbs.filmix.co/posters/orig/shesteyro-vne-zakona_2019_138413_0.jpg", "https://youtu.be/-6jCNhebfg8"))
//        movieList.add(Movie("Встреча", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fermentum massa sem. Maecenas tempus pretium accumsan. Maecenas sit amet accumsan neque, quis euismod massa. Donec mi est, viverra sit amet sollicitudin vitae, volutpat ac odio. Etiam et semper eros, quis malesuada nisi. Mauris gravida turpis a dolor rutrum congue. Sed vestibulum enim eget lectus lacinia, in scelerisque magna aliquam.", "https://thumbs.filmix.co/posters/orig/vstrecha_2018_126131_0.jpg", "https://youtu.be/-6jCNhebfg8"))
//        movieList.add(Movie("Колдовство: Новый ритуал", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fermentum massa sem. Maecenas tempus pretium accumsan. Maecenas sit amet accumsan neque, quis euismod massa. Donec mi est, viverra sit amet sollicitudin vitae, volutpat ac odio. Etiam et semper eros, quis malesuada nisi. Mauris gravida turpis a dolor rutrum congue. Sed vestibulum enim eget lectus lacinia, in scelerisque magna aliquam.", "https://s.kinokrad.co/uploads/img/6e298da7763129fe1d8e73c9862b6978.jpeg", "https://youtu.be/-6jCNhebfg8"))
//        movieList.add(Movie("Платформа", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fermentum massa sem. Maecenas tempus pretium accumsan. Maecenas sit amet accumsan neque, quis euismod massa. Donec mi est, viverra sit amet sollicitudin vitae, volutpat ac odio. Etiam et semper eros, quis malesuada nisi. Mauris gravida turpis a dolor rutrum congue. Sed vestibulum enim eget lectus lacinia, in scelerisque magna aliquam.", "https://upload.wikimedia.org/wikipedia/ru/7/72/El_hoyo_%282019%29.jpg", "https://youtu.be/-6jCNhebfg8"))

//        movieList.add(Movie("Платформа", "", "https://firebasestorage.googleapis.com/v0/b/myunicanteen-2015b.appspot.com/o/chorizo.jpg?alt=media&token=0d64d905-8647-46ca-8a90-5d60ebc03954"))
//        movieList.add(Movie("Шестеро вне закона", "", "https://firebasestorage.googleapis.com/v0/b/myunicanteen-2015b.appspot.com/o/chorizo.jpg?alt=media&token=0d64d905-8647-46ca-8a90-5d60ebc03954"))
//        movieList.add(Movie("Встреча", "", "https://firebasestorage.googleapis.com/v0/b/myunicanteen-2015b.appspot.com/o/chorizo.jpg?alt=media&token=0d64d905-8647-46ca-8a90-5d60ebc03954"))
//        movieList.add(Movie("Колдовство: Новый ритуал", "", "https://firebasestorage.googleapis.com/v0/b/myunicanteen-2015b.appspot.com/o/chorizo.jpg?alt=media&token=0d64d905-8647-46ca-8a90-5d60ebc03954"))
//        movieList.add(Movie("Платформа", "", "https://firebasestorage.googleapis.com/v0/b/myunicanteen-2015b.appspot.com/o/chorizo.jpg?alt=media&token=0d64d905-8647-46ca-8a90-5d60ebc03954"))

//        val sliderPagerAdapter = SliderPagerAdapter(this, slideList)
//        slidePager.adapter = sliderPagerAdapter
//
//        val timer = Timer()
//        timer.scheduleAtFixedRate(SliderTask(), 4000, 6000)
//
//        indicator.setupWithViewPager(slidePager, true)
//
//        val movieAdapter = MovieAdapter(this, movieList, this)
//        val movie_recycler_view = findViewById<RecyclerView>(R.id.movie_recycler_view)
//        movie_recycler_view.adapter = movieAdapter
//        movie_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun searchMovieApi(query: String, pageNum: Int) {
        movieListViewModel.searchMovieApi(query, pageNum)
    }

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

//    inner class SliderTask : TimerTask() {
//
//        override fun run() {
//            runOnUiThread {
//                if (slidePager.currentItem < slideList.size - 1)
//                    slidePager.currentItem = slidePager.currentItem + 1
//                else
//                    slidePager.currentItem = 0
//            }
//        }
//
//    }

    private fun observeAnyChange() {
        movieListViewModel.getMovies().observe(this, androidx.lifecycle.Observer { movieModels ->
            movieModels?.forEach { _ ->
//                Log.v("Tag", "onChange: ${it.title}")
                movieAdapter.data = movieModels
            }
        })
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

    private fun prepareRecView() {
        movieAdapter = MovieAdapter(this)

        recyclerView.adapter = movieAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onMovieClick(position: Int) {
        Toast.makeText(this, movieListViewModel.getMovies().value?.get(position)?.title, Toast.LENGTH_SHORT).show()
    }

    override fun onCategoryClick(category: String) {
        TODO("Not yet implemented")
    }

}