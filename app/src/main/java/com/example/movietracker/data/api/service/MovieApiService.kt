package com.example.movietracker.data.api.service

import com.example.movietracker.data.api.model.AddMovieRequest
import com.example.movietracker.data.api.model.MovieDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface for movie operations.
 *
 * This interface defines the API endpoints and their return types.
 * It follows the Service pattern for network operations and is implemented
 * by Retrofit at runtime.
 */
interface MovieApiService {
    /**
     * Get all movies.
     * @return List of movie DTOs.
     */
    @GET("movies")
    suspend fun getAllMovies(): List<MovieDto>

    /**
     * Search for movies by title.
     * @param query The search query.
     * @return List of matching movie DTOs.
     */
    @GET("movies")
    suspend fun searchMovies(@Query("search") query: String): List<MovieDto>

    /**
     * Get a movie by its ID.
     * @param movieId The ID of the movie to retrieve.
     * @return The movie DTO with the specified ID.
     */
    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") movieId: Int): MovieDto

    /**
     * Add a new movie.
     * @param movieRequest The movie data to add.
     * @return The newly added movie DTO.
     */
    @POST("movies")
    suspend fun addMovie(@Body movieRequest: AddMovieRequest): MovieDto
}