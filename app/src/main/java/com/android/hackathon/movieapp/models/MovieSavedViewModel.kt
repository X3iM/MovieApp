package com.android.hackathon.movieapp.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.hackathon.movieapp.repositories.MovieSavedRepository
import com.android.hackathon.movieapp.room.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieSavedViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Movie>>
    private val repo: MovieSavedRepository

    init {
        val movieDao = MovieDatabase.getDatabase(application).movieDao()
        repo = MovieSavedRepository(movieDao)
        readAllData = repo.readAllData
    }

    fun addUser(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addMovie(movie)
        }
    }

    fun getAllSavedMovie(): LiveData<List<Movie>> {
        return readAllData
    }

//    inner class Factory(private var application: Application) : ViewModelProvider.NewInstanceFactory() {
//
//        @Override
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return MovieSavedVieModel(application) as T
//        }
//
//    }

}