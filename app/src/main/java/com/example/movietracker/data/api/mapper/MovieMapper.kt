package com.example.movietracker.data.api.mapper

import com.example.movietracker.data.api.model.MovieDto
import com.example.movietracker.data.db.entity.MovieEntity
import com.example.movietracker.domain.model.Movie
import javax.inject.Inject

/**
 * Mapper class for converting between API DTO and domain models.
 *
 * This class follows the Mapper pattern and is responsible for converting
 * between the different movie representations across layers.
 */
class MovieMapper @Inject constructor() {

    /**
     * Convert a Movie DTO to an Entity
     */
    fun dtoToEntity(dto: MovieDto): MovieEntity {
        return MovieEntity(
            id = dto.id,
            title = dto.title,
            year = dto.year,
            imageUrl = dto.imageUrl,
            isFavorite = false,
            isWatched = false
        )
    }

    /**
     * Convert a Movie DTO to a Domain model
     */
    fun dtoToDomain(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            year = dto.year,
            imageUrl = dto.imageUrl,
            isFavorite = false,
            isWatched = false
        )
    }

    /**
     * Convert a list of Movie DTOs to a list of entities
     */
    fun dtoListToEntityList(dtos: List<MovieDto>): List<MovieEntity> {
        return dtos.map { dtoToEntity(it) }
    }

    /**
     * Convert an Entity to a Domain model
     */
    fun entityToDomain(entity: MovieEntity): Movie {
        return Movie(
            id = entity.id,
            title = entity.title,
            year = entity.year,
            imageUrl = entity.imageUrl,
            isFavorite = entity.isFavorite,
            isWatched = entity.isWatched
        )
    }

    /**
     * Convert a Domain model to an Entity
     */
    fun domainToEntity(domain: Movie): MovieEntity {
        return MovieEntity(
            id = domain.id,
            title = domain.title,
            year = domain.year,
            imageUrl = domain.imageUrl,
            isFavorite = domain.isFavorite,
            isWatched = domain.isWatched
        )
    }

    /**
     * Convert a list of Entities to a list of Domain models
     */
    fun entityListToDomainList(entities: List<MovieEntity>): List<Movie> {
        return entities.map { entityToDomain(it) }
    }
}