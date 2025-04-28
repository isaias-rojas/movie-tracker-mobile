package com.example.movietracker.domain.usecase

import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the details of a specific movie.
 *
 * This class implements the BaseUseCase interface with an Int parameter for the movie ID
 * and returns a Flow of the Movie details. It follows the Single Responsibility Principle
 * by focusing only on retrieving a specific movie's details.
 */
class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<Int, Flow<Movie?>> {

    override suspend fun invoke(params: Int): Flow<Movie?> {
        return movieRepository.getMovieById(params)
    }
}