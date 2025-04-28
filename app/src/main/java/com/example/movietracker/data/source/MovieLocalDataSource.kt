package com.example.movietracker.data.source

import com.example.movietracker.data.db.dao.MovieDao
import com.example.movietracker.data.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local data source for movie data.
 *
 * This class is responsible for handling all local data operations
 * related to movies using Room database. It follows the Data Source pattern
 * which abstracts the data origin from the repository.
 */
class MovieLocalDataSource @Inject constructor(
    private val movieDao: MovieDao
) {
    /**
     * Get all movies from the local database.
     * @return A Flow emitting a list of all movies.
     */
    fun getAllMovies(): Flow<List<MovieEntity>> {
        return movieDao.getAllMovies()
    }

    /**
     * Get a movie by its ID.
     * @param movieId The ID of the movie to retrieve.
     * @return The movie with the specified ID, or null if not found.
     */
    suspend fun getMovieById(movieId: Int): MovieEntity? {
        return movieDao.getMovieById(movieId)
    }

    /**
     * Get a movie by its ID as a Flow.
     * @param movieId The ID of the movie to retrieve.
     * @return A Flow emitting the movie with the specified ID, or null if not found.
     */
    fun getMovieByIdAsFlow(movieId: Int): Flow<MovieEntity?> {
        return movieDao.getMovieByIdAsFlow(movieId)
    }

    /**
     * Get all favorite movies.
     * @return A Flow emitting a list of all favorite movies.
     */
    fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return movieDao.getFavoriteMovies()
    }

    /**
     * Get all watched movies.
     * @return A Flow emitting a list of all watched movies.
     */
    fun getWatchedMovies(): Flow<List<MovieEntity>> {
        return movieDao.getWatchedMovies()
    }

    /**
     * Save a movie to the local database.
     * @param movie The movie to save.
     */
    suspend fun saveMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    /**
     * Save multiple movies to the local database.
     * @param movies The list of movies to save.
     */
    suspend fun saveMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    /**
     * Update a movie in the local database.
     * @param movie The updated movie information.
     */
    suspend fun updateMovie(movie: MovieEntity) {
        movieDao.updateMovie(movie)
    }

    /**
     * Toggle the favorite status of a movie.
     * @param movieId The ID of the movie.
     * @param isFavorite The new favorite status.
     */
    suspend fun toggleFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }

    /**
     * Toggle the watched status of a movie.
     * @param movieId The ID of the movie.
     * @param isWatched The new watched status.
     */
    suspend fun toggleWatchedStatus(movieId: Int, isWatched: Boolean) {
        movieDao.updateWatchedStatus(movieId, isWatched)
    }

    /**
     * Delete all movies from the local database.
     */
    suspend fun deleteAllMovies() {
        movieDao.deleteAllMovies()
    }
}