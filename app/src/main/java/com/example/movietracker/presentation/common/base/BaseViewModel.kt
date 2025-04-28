package com.example.movietracker.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class providing common functionality.
 *
 * This class extends AndroidX ViewModel and provides common functionality
 * for state management and side effects. It follows the MVVM architectural pattern.
 *
 * @param S The type of the UI state
 * @param E The type of UI events (side effects)
 */
abstract class BaseViewModel<S, E> : ViewModel() {

    /**
     * The current UI state.
     */
    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<S> = _state

    /**
     * One-time events/side effects.
     */
    private val _event = MutableSharedFlow<E>()
    val event: SharedFlow<E> = _event

    /**
     * Define the initial state for this ViewModel.
     */
    abstract fun initialState(): S

    /**
     * Update the current state.
     *
     * @param reducer A lambda that takes the current state and returns a new state.
     */
    protected fun setState(reducer: (S) -> S) {
        val newState = reducer(_state.value)
        _state.value = newState
    }

    /**
     * Emit a one-time event/side effect.
     *
     * @param event The event to emit.
     */
    protected fun emitEvent(event: E) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    /**
     * Launch a coroutine in the ViewModel scope.
     *
     * @param block The coroutine block to execute.
     */
    protected fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }
}