package com.example.movietracker.domain.usecase

import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case for searching movies based on a query.
 *
 * This class implements the BaseUseCase interface with a String parameter for the search query
 * and returns a list of Movies. It follows the Single Responsibility Principle by focusing only
 * on searching for movies based on a query.
 */
class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<String, List<Movie>> {

    override suspend fun invoke(params: String): List<Movie> {
        return movieRepository.searchMovies(params)
    }
}