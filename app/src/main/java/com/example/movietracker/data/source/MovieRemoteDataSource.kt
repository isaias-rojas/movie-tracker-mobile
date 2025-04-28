package com.example.movietracker.data.source

import com.example.movietracker.data.api.model.AddMovieRequest
import com.example.movietracker.data.api.model.MovieDto
import com.example.movietracker.data.api.service.MovieApiService
import javax.inject.Inject

/**
 * Remote data source for movie data.
 *
 * This class is responsible for handling all remote data operations
 * related to movies using the API service. It follows the Data Source pattern
 * which abstracts the data origin from the repository.
 */
class MovieRemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) {
    /**
     * Get all movies from the API.
     * @return List of movie DTOs.
     */
    suspend fun getAllMovies(): List<MovieDto> {
        return movieApiService.getAllMovies()
    }

    /**
     * Search for movies by title.
     * @param query The search query.
     * @return List of matching movie DTOs.
     */
    suspend fun searchMovies(query: String): List<MovieDto> {
        return movieApiService.searchMovies(query)
    }

    /**
     * Get a movie by its ID.
     * @param movieId The ID of the movie to retrieve.
     * @return The movie DTO with the specified ID.
     */
    suspend fun getMovieById(movieId: Int): MovieDto {
        return movieApiService.getMovieById(movieId)
    }

    /**
     * Add a new movie.
     * @param title The title of the movie.
     * @param year The release year of the movie.
     * @return The newly added movie DTO.
     */
    suspend fun addMovie(title: String, year: Int): MovieDto {
        val request = AddMovieRequest(title, year)
        return movieApiService.addMovie(request)
    }
}