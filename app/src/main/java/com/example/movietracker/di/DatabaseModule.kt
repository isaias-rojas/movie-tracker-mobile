package com.example.movietracker.di

import android.content.Context
import androidx.room.Room
import com.example.movietracker.data.db.MovieDatabase
import com.example.movietracker.data.db.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 *
 * This module follows the Dependency Injection pattern and provides
 * the Room database and DAO instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provide the Room database instance.
     *
     * @param context The application context.
     * @return A singleton instance of the MovieDatabase.
     */
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).build()
    }

    /**
     * Provide the Movie DAO.
     *
     * @param database The database instance.
     * @return The Movie DAO.
     */
    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
}