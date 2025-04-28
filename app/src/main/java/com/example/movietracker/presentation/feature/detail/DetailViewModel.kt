package com.example.movietracker.presentation.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.usecase.GetMovieDetailsUseCase
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
 * ViewModel for the Movie Detail screen.
 *
 * This ViewModel manages the state for the detail screen which displays a single movie's details.
 * It follows the MVVM pattern and interacts with use cases to perform operations.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val toggleWatchedUseCase: ToggleWatchedUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailViewModel.DetailState, DetailViewModel.DetailEvent>() {

    /**
     * State for the Detail screen.
     *
     * @property movieState The state of the movie details
     */
    data class DetailState(
        val movieState: UiState<Movie?> = UiState.Idle
    )

    /**
     * Events for the Detail screen.
     */
    sealed class DetailEvent {
        /**
         * Navigate back to the previous screen.
         */
        data object NavigateBack : DetailEvent()

        /**
         * Show an error message.
         * @property message The error message to show
         */
        data class ShowError(val message: String) : DetailEvent()
    }

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    override fun initialState(): DetailState = DetailState()

    init {
        loadMovieDetails()
    }

    /**
     * Load movie details.
     */
    fun loadMovieDetails() {
        viewModelScope.launch {
            setState { it.copy(movieState = UiState.Loading) }

            getMovieDetailsUseCase(movieId)
                .onEach { movie ->
                    setState { it.copy(movieState = UiState.Success(movie)) }
                }
                .catch { error ->
                    setState { it.copy(movieState = UiState.Error(error.message ?: "Unknown error")) }
                    emitEvent(DetailEvent.ShowError(error.message ?: "Failed to load movie details"))
                }
                .launchIn(this)
        }
    }

    /**
     * Toggle the favorite status of the movie.
     */
    fun toggleFavorite() {
        val currentMovie = (state.value.movieState as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            toggleFavoriteUseCase(
                ToggleFavoriteUseCase.Params(
                    movieId = currentMovie.id,
                    isFavorite = !currentMovie.isFavorite
                )
            )
        }
    }

    /**
     * Toggle the watched status of the movie.
     */
    fun toggleWatched() {
        val currentMovie = (state.value.movieState as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            toggleWatchedUseCase(
                ToggleWatchedUseCase.Params(
                    movieId = currentMovie.id,
                    isWatched = !currentMovie.isWatched
                )
            )
        }
    }

    /**
     * Navigate back to the previous screen.
     */
    fun onBackPressed() {
        emitEvent(DetailEvent.NavigateBack)
    }
}