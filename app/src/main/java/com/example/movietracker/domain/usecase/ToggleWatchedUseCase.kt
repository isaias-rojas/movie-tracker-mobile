package com.example.movietracker.domain.usecase

import com.example.movietracker.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case for toggling the watched status of a movie.
 *
 * This class implements the BaseUseCaseNoResult interface with a Pair parameter for the movie ID
 * and the watched status. It follows the Single Responsibility Principle by focusing only on
 * updating a movie's watched status.
 */
class ToggleWatchedUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCaseNoResult<ToggleWatchedUseCase.Params> {

    data class Params(val movieId: Int, val isWatched: Boolean)

    override suspend fun invoke(params: Params) {
        movieRepository.toggleWatchedStatus(params.movieId, params.isWatched)
    }
}