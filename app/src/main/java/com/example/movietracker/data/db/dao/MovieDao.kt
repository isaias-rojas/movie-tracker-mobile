package com.example.movietracker.data.db.dao

import androidx.room.*
import com.example.movietracker.data.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Movie entity.
 *
 * This interface defines all the database operations for the Movie entity.
 * It follows the DAO pattern which provides a clean API for accessing the database.
 */
@Dao
interface MovieDao {
    /**
     * Get all movies from the database.
     * @return A Flow emitting a list of all movies in the database.
     */
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    /**
     * Get a movie by its ID.
     * @param movieId The ID of the movie to retrieve.
     * @return The movie with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    /**
     * Get a movie by its ID as a Flow.
     * @param movieId The ID of the movie to retrieve.
     * @return A Flow emitting the movie with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieByIdAsFlow(movieId: Int): Flow<MovieEntity?>

    /**
     * Get all favorite movies.
     * @return A Flow emitting a list of all favorite movies.
     */
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    /**
     * Get all watched movies.
     * @return A Flow emitting a list of all watched movies.
     */
    @Query("SELECT * FROM movies WHERE isWatched = 1")
    fun getWatchedMovies(): Flow<List<MovieEntity>>

    /**
     * Insert a movie into the database.
     * @param movie The movie to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    /**
     * Insert multiple movies into the database.
     * @param movies The list of movies to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    /**
     * Update a movie in the database.
     * @param movie The updated movie information.
     */
    @Update
    suspend fun updateMovie(movie: MovieEntity)

    /**
     * Update the favorite status of a movie.
     * @param movieId The ID of the movie.
     * @param isFavorite The new favorite status.
     */
    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean)

    /**
     * Update the watched status of a movie.
     * @param movieId The ID of the movie.
     * @param isWatched The new watched status.
     */
    @Query("UPDATE movies SET isWatched = :isWatched WHERE id = :movieId")
    suspend fun updateWatchedStatus(movieId: Int, isWatched: Boolean)

    /**
     * Delete a movie from the database.
     * @param movieId The ID of the movie to delete.
     */
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)

    /**
     * Delete all movies from the database.
     */
    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}