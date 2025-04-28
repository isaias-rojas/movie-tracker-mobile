package com.example.movietracker.di

import com.example.movietracker.data.repository.MovieRepositoryImpl
import com.example.movietracker.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Application-level Hilt module providing top-level dependencies.
 *
 * This module follows the Dependency Injection pattern and provides
 * application-scoped dependencies, particularly binding repository
 * interfaces to their implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Bind MovieRepository interface to its implementation.
     *
     * @param repositoryImpl The implementation to bind to the interface.
     * @return The bound repository.
     */
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        repositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}