package com.example.movietracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movietracker.data.db.dao.MovieDao
import com.example.movietracker.data.db.entity.MovieEntity

/**
 * Room Database class for the Movie Tracker app.
 *
 * This abstract class defines the database configuration and provides
 * access to the DAOs. It follows the Database pattern in Room.
 */
@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    /**
     * Get the Movie DAO.
     * @return The Movie DAO for accessing movie data in the database.
     */
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "movie_tracker_db"
    }
}