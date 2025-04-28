package com.example.movietracker.presentation.feature.search

import androidx.lifecycle.viewModelScope
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.usecase.SearchMoviesUseCase
import com.example.movietracker.domain.usecase.ToggleFavoriteUseCase
import com.example.movietracker.domain.usecase.ToggleWatchedUseCase
import com.example.movietracker.presentation.common.base.BaseViewModel
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Search screen.
 *
 * This ViewModel manages the state for the search screen which allows users
 * to search for movies. It follows the MVVM pattern and interacts with use cases.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val toggleWatchedUseCase: ToggleWatchedUseCase
) : BaseViewModel<SearchViewModel.SearchState, SearchViewModel.SearchEvent>() {

    /**
     * State for the Search screen.
     *
     * @property searchQuery The current search query
     * @property searchResults The state of the search results
     */
    data class SearchState(
        val searchQuery: String = "",
        val searchResults: UiState<List<Movie>> = UiState.Idle
    )

    /**
     * Events for the Search screen.
     */
    sealed class SearchEvent {
        /**
         * Navigate to movie details screen.
         * @property movieId The ID of the movie to show details for
         */
        data class NavigateToMovieDetails(val movieId: Int) : SearchEvent()

        /**
         * Show an error message.
         * @property message The error message to show
         */
        data class ShowError(val message: String) : SearchEvent()
    }

    override fun initialState(): SearchState = SearchState()

    private var searchJob: Job? = null

    /**
     * Update the search query and perform the search after a delay.
     *
     * @param query The new search query
     */
    fun updateSearchQuery(query: String) {
        setState { it.copy(searchQuery = query) }

        // Cancel previous search job if exists
        searchJob?.cancel()

        if (query.length >= 2) {
            // Start a new search job with debounce
            searchJob = viewModelScope.launch {
                delay(300) // Debounce for 300ms
                search(query)
            }
        } else if (query.isEmpty()) {
            // Clear results when query is empty
            setState { it.copy(searchResults = UiState.Idle) }
        }
    }

    /**
     * Clear the search results and query
     */
    fun clearSearch() {
        searchJob?.cancel()
        setState { it.copy(
            searchQuery = "",
            searchResults = UiState.Idle
        )}
    }

    /**
     * Perform an immediate search.
     */
    fun performSearch() {
        val query = state.value.searchQuery
        if (query.length >= 2) {
            search(query)
        }
    }

    /**
     * Search for movies based on the query.
     *
     * @param query The search query
     */
    private fun search(query: String) {
        viewModelScope.launch {
            setState { it.copy(searchResults = UiState.Loading) }

            try {
                val results = searchMoviesUseCase(query)
                setState { it.copy(searchResults = UiState.Success(results)) }
            } catch (e: Exception) {
                setState { it.copy(searchResults = UiState.Error(e.message ?: "Unknown error")) }
                emitEvent(SearchEvent.ShowError(e.message ?: "Search failed"))
            }
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
        emitEvent(SearchEvent.NavigateToMovieDetails(movie.id))
    }
}