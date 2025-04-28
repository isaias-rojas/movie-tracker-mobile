package com.example.movietracker.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) for a movie from the API.
 *
 * This class represents the model returned by the API and is used by Retrofit.
 * It follows the DTO pattern which separates the data layer model from domain models.
 */
data class MovieDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("year")
    val year: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
)

/**
 * DTO for creating a new movie with the API.
 */
data class AddMovieRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("year")
    val year: Int
)