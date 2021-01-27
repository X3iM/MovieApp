package com.android.hackathon.movieapp.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.hackathon.movieapp.models.Movie

@Database(version = 1, entities = [Movie::class], exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            val temp = INSTANCE
            if (temp != null) return temp
            synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "Movie"
                ).build()
                INSTANCE = inst
                return inst
            }
        }
    }

//    abstract fun getMovieDao(): MovieDao
}