package com.example.movietracker.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.usecase.GetAllMoviesUseCase
import com.example.movietracker.domain.usecase.ToggleFavoriteUseCase
import com.example.movietracker.domain.usecase.ToggleWatchedUseCase
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 *
 * This ViewModel manages the state for the home screen which displays all movies.
 * It follows the MVVM pattern and interacts with use cases to perform operations.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val toggleWatchedUseCase: ToggleWatchedUseCase
) : ViewModel() {

    /**
     * State for the Home screen.
     *
     * @property moviesState The state of the movies list
     */
    data class HomeState(
        val moviesState: UiState<List<Movie>> = UiState.Idle
    )

    /**
     * Events for the Home screen.
     */
    sealed class HomeEvent {
        /**
         * Navigate to movie details screen.
         * @property movieId The ID of the movie to show details for
         */
        data class NavigateToMovieDetails(val movieId: Int) : HomeEvent()

        /**
         * Show an error message.
         * @property message The error message to show
         */
        data class ShowError(val message: String) : HomeEvent()
    }

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event

    init {
        loadMovies()
    }

    /**
     * Load all movies.
     */
    fun loadMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(moviesState = UiState.Loading)

            getAllMoviesUseCase()
                .onEach { movies ->
                    _state.value = _state.value.copy(moviesState = UiState.Success(movies))
                }
                .catch { error ->
                    _state.value = _state.value.copy(moviesState = UiState.Error(error.message ?: "Unknown error"))
                    _event.emit(HomeEvent.ShowError(error.message ?: "Failed to load movies"))
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
        viewModelScope.launch {
            _event.emit(HomeEvent.NavigateToMovieDetails(movie.id))
        }
    }
}