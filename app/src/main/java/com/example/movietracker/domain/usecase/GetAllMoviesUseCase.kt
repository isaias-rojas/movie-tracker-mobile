package com.example.movietracker.domain.usecase

import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all movies.
 *
 * This class implements the BaseUseCaseNoParams interface and follows the
 * Single Responsibility Principle by focusing only on retrieving all movies.
 * It delegates to the MovieRepository to get the data.
 */
class GetAllMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCaseNoParams<Flow<List<Movie>>> {

    override suspend fun invoke(): Flow<List<Movie>> {
        return movieRepository.getAllMovies()
    }
}