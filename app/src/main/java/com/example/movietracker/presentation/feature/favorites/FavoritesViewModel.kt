package com.example.movietracker.presentation.feature.favorites

import androidx.lifecycle.viewModelScope
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.usecase.GetFavoriteMoviesUseCase
import com.example.movietracker.domain.usecase.ToggleFavoriteUseCase
import com.example.movietracker.domain.usecase.ToggleWatchedUseCase
import com.example.movietracker.presentation.common.base.BaseViewModel
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Favorites screen.
 *
 * This ViewModel manages the state for the favorites screen which displays
 * movies marked as favorites. It follows the MVVM pattern and interacts with use cases.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val toggleWatchedUseCase: ToggleWatchedUseCase
) : BaseViewModel<FavoritesViewModel.FavoritesState, FavoritesViewModel.FavoritesEvent>() {

    /**
     * State for the Favorites screen.
     *
     * @property favoritesState The state of the favorites list
     */
    data class FavoritesState(
        val favoritesState: UiState<List<Movie>> = UiState.Idle
    )

    /**
     * Events for the Favorites screen.
     */
    sealed class FavoritesEvent {
        /**
         * Navigate to movie details screen.
         * @property movieId The ID of the movie to show details for
         */
        data class NavigateToMovieDetails(val movieId: Int) : FavoritesEvent()

        /**
         * Show an error message.
         * @property message The error message to show
         */
        data class ShowError(val message: String) : FavoritesEvent()
    }

    override fun initialState(): FavoritesState = FavoritesState()

    init {
        loadFavoriteMovies()
    }

    /**
     * Load favorite movies.
     */
    fun loadFavoriteMovies() {
        viewModelScope.launch {
            setState { it.copy(favoritesState = UiState.Loading) }

            getFavoriteMoviesUseCase()
                .onEach { movies ->
                    setState { it.copy(favoritesState = UiState.Success(movies)) }
                }
                .catch { error ->
                    setState { it.copy(favoritesState = UiState.Error(error.message ?: "Unknown error")) }
                    emitEvent(FavoritesEvent.ShowError(error.message ?: "Failed to load favorite movies"))
                }
                .launchIn(this)
        }
    }

    /**
     * Toggle the favorite status of a movie.
     *
     * @param movie The movie to toggle favorite status for
     */
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavoriteUseCase(ToggleFavoriteUseCase.Params(movie.id, !movie.isFavorite))
        }
    }

    /**
     * Toggle the watched status of a movie.
     *
     * @param movie The movie to toggle watched status for
     */
    fun toggleWatched(movie: Movie) {
        viewModelScope.launch {
            toggleWatchedUseCase(ToggleWatchedUseCase.Params(movie.id, !movie.isWatched))
        }
    }

    /**
     * Navigate to movie details.
     *
     * @param movie The movie to show details for
     */
    fun onMovieClicked(movie: Movie) {
        emitEvent(FavoritesEvent.NavigateToMovieDetails(movie.id))
    }
}