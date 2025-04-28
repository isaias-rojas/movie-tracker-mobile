package com.example.movietracker.domain.repository

import com.example.movietracker.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing movie data.
 *
 * This interface follows the Repository pattern and defines methods for accessing and manipulating
 * movie data. It abstracts the data sources (local database and remote API) from the rest of the application.
 * The implementation of this interface will handle the coordination between different data sources.
 */
interface MovieRepository {
    /**
     * Get all movies.
     * @return A flow that emits a list of all movies
     */
    fun getAllMovies(): Flow<List<Movie>>

    /**
     * Get a specific movie by ID.
     * @param movieId The ID of the movie to retrieve
     * @return A flow that emits the movie with the specified ID
     */
    fun getMovieById(movieId: Int): Flow<Movie?>

    /**
     * Search for movies based on a query string.
     * @param query The search query
     * @return A flow that emits a list of movies matching the query
     */
    suspend fun searchMovies(query: String): List<Movie>

    /**
     * Refresh movies from the remote data source.
     * @return True if the refresh was successful
     */
    suspend fun refreshMovies(): Boolean

    /**
     * Toggle the favorite status of a movie.
     * @param movieId The ID of the movie
     * @param isFavorite The new favorite status
     */
    suspend fun toggleFavoriteStatus(movieId: Int, isFavorite: Boolean)

    /**
     * Toggle the watched status of a movie.
     * @param movieId The ID of the movie
     * @param isWatched The new watched status
     */
    suspend fun toggleWatchedStatus(movieId: Int, isWatched: Boolean)

    /**
     * Get all favorite movies.
     * @return A flow that emits a list of favorite movies
     */
    fun getFavoriteMovies(): Flow<List<Movie>>

    /**
     * Get all watched movies.
     * @return A flow that emits a list of watched movies
     */
    fun getWatchedMovies(): Flow<List<Movie>>

    /**
     * Add a new movie.
     * @param title The title of the movie
     * @param year The release year of the movie
     * @return The newly added movie
     */
    suspend fun addMovie(title: String, year: Int): Movie?
}