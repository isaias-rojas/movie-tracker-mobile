package com.example.movietracker.presentation.common.model

/**
 * Sealed class representing the various states of a UI component.
 *
 * This follows the State pattern and helps standardize UI state management
 * across the app.
 *
 * @param T The type of data in the Success state
 */
sealed class UiState<out T> {
    /**
     * Initial idle state.
     */
    data object Idle : UiState<Nothing>()

    /**
     * Loading state.
     */
    data object Loading : UiState<Nothing>()

    /**
     * Success state with data.
     *
     * @param data The data
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Error state with error message.
     *
     * @param message The error message
     */
    data class Error(val message: String) : UiState<Nothing>()
}