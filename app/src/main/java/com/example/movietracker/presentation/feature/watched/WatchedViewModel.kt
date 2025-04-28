package com.example.movietracker.presentation.feature.watched

import androidx.lifecycle.viewModelScope
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.usecase.GetWatchedMoviesUseCase
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
 * ViewModel for the Watched screen.
 *
 * This ViewModel manages the state for the watched screen which displays
 * movies marked as watched. It follows the MVVM pattern and interacts with use cases.
 */
@HiltViewModel
class WatchedViewModel @Inject constructor(
    private val getWatchedMoviesUseCase: GetWatchedMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val toggleWatchedUseCase: ToggleWatchedUseCase
) : BaseViewModel<WatchedViewModel.WatchedState, WatchedViewModel.WatchedEvent>() {

    /**
     * State for the Watched screen.
     *
     * @property watchedState The state of the watched movies list
     */
    data class WatchedState(
        val watchedState: UiState<List<Movie>> = UiState.Idle
    )

    /**
     * Events for the Watched screen.
     */
    sealed class WatchedEvent {
        /**
         * Navigate to movie details screen.
         * @property movieId The ID of the movie to show details for
         */
        data class NavigateToMovieDetails(val movieId: Int) : WatchedEvent()

        /**
         * Show an error message.
         * @property message The error message to show
         */
        data class ShowError(val message: String) : WatchedEvent()
    }

    override fun initialState(): WatchedState = WatchedState()

    init {
        loadWatchedMovies()
    }

    /**
     * Load watched movies.
     */
    fun loadWatchedMovies() {
        viewModelScope.launch {
            setState { it.copy(watchedState = UiState.Loading) }

            getWatchedMoviesUseCase()
                .onEach { movies ->
                    setState { it.copy(watchedState = UiState.Success(movies)) }
                }
                .catch { error ->
                    setState { it.copy(watchedState = UiState.Error(error.message ?: "Unknown error")) }
                    emitEvent(WatchedEvent.ShowError(error.message ?: "Failed to load watched movies"))
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
        emitEvent(WatchedEvent.NavigateToMovieDetails(movie.id))
    }
}