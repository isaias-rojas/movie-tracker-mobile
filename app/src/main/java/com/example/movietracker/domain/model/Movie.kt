package com.example.movietracker.domain.model

/**
 * Domain model representing a movie.
 *
 * This is the core business model that will be used across the application.
 * It contains only the essential properties needed for the business logic and UI.
 *
 * @property id Unique identifier for the movie
 * @property title The title of the movie
 * @property year The release year of the movie
 * @property imageUrl The URL of the movie poster image
 * @property isFavorite Indicates if the movie is marked as favorite by the user
 * @property isWatched Indicates if the movie is marked as watched by the user
 */
data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)