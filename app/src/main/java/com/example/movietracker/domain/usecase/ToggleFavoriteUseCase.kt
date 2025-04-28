package com.example.movietracker.domain.usecase

import com.example.movietracker.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case for toggling the favorite status of a movie.
 *
 * This class implements the BaseUseCaseNoResult interface with a Pair parameter for the movie ID
 * and the favorite status. It follows the Single Responsibility Principle by focusing only on
 * updating a movie's favorite status.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCaseNoResult<ToggleFavoriteUseCase.Params> {

    data class Params(val movieId: Int, val isFavorite: Boolean)

    override suspend fun invoke(params: Params) {
        movieRepository.toggleFavoriteStatus(params.movieId, params.isFavorite)
    }
}