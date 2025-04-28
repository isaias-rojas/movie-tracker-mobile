package com.example.movietracker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class for the Room database representing a movie.
 *
 * This class follows the Entity pattern for Room database and
 * is used to store movie data locally.
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val year: Int,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)