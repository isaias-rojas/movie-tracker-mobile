package com.example.movietracker.domain.usecase

/**
 * Base interface for use cases that take parameters and return a result.
 *
 * This interface follows the Command pattern and the Single Responsibility Principle.
 * Each use case represents a single action or business rule in the application.
 *
 * @param P The type of the input parameters
 * @param R The type of the result
 */
interface BaseUseCase<in P, out R> {
    suspend operator fun invoke(params: P): R
}

/**
 * Base interface for use cases that don't take parameters but return a result.
 *
 * @param R The type of the result
 */
interface BaseUseCaseNoParams<out R> {
    suspend operator fun invoke(): R
}

/**
 * Base interface for use cases that take parameters but don't return a result.
 *
 * @param P The type of the input parameters
 */
interface BaseUseCaseNoResult<in P> {
    suspend operator fun invoke(params: P)
}