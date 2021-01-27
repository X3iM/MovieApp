package com.android.hackathon.movieapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.hackathon.movieapp.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(movie: Movie)

    @Query("SELECT * FROM Movie ORDER BY id ASC")
    fun getSavedMovies() : LiveData<List<Movie>>

}