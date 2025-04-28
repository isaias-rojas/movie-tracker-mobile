package com.example.movietracker.data.repository

import com.example.movietracker.data.api.mapper.MovieMapper
import com.example.movietracker.data.source.MovieLocalDataSource
import com.example.movietracker.data.source.MovieRemoteDataSource
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of MovieRepository that connects data sources.
 *
 * This class follows the Repository pattern and serves as a single source
 * of truth for movie data. It coordinates between local and remote data sources
 * following the offline-first approach.
 */
class MovieRepositoryImpl @Inject constructor(
    private val localDataSource: MovieLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val mapper: MovieMapper
) : MovieRepository {

    override fun getAllMovies(): Flow<List<Movie>> {
        return localDataSource.getAllMovies().map { entities ->
            mapper.entityListToDomainList(entities)
        }
    }

    override fun getMovieById(movieId: Int): Flow<Movie?> {
        return localDataSource.getMovieByIdAsFlow(movieId).map { entity ->
            entity?.let { mapper.entityToDomain(it) }
        }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        try {
            // 1. Get movies from API
            val remoteMovies = remoteDataSource.searchMovies(query)

            // 2. Get existing movies from local DB to preserve their states
            val localMovies = localDataSource.getAllMovies().first()

            // 3. Create a map of existing movie states (favorite/watched)
            val stateMap = localMovies.associate {
                it.id to Pair(it.isFavorite, it.isWatched)
            }

            // 4. Preserve states when converting to entities
            val entitiesToSave = remoteMovies.map { dto ->
                val entity = mapper.dtoToEntity(dto)
                val (isFavorite, isWatched) = stateMap[entity.id] ?: (false to false)
                entity.copy(isFavorite = isFavorite, isWatched = isWatched)
            }

            // 5. Save to local DB with preserved states
            localDataSource.saveMovies(entitiesToSave)

            // 6. Return results with correct states
            return mapper.entityListToDomainList(
                localDataSource.getAllMovies().first().filter {
                        entity -> remoteMovies.any { it.id == entity.id }
                }
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun refreshMovies(): Boolean {
        return try {
            val remoteMovies = remoteDataSource.getAllMovies()
            val currentEntities = localDataSource.getAllMovies().first()

            val statusMap = currentEntities.associate { entity ->
                entity.id to Pair(entity.isFavorite, entity.isWatched)
            }

            val entitiesToSave = remoteMovies.map { dto ->
                val entity = mapper.dtoToEntity(dto)
                val (isFavorite, isWatched) = statusMap[entity.id] ?: (false to false)
                entity.copy(
                    isFavorite = isFavorite,
                    isWatched = isWatched
                )
            }

            localDataSource.saveMovies(entitiesToSave)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun toggleFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        localDataSource.toggleFavoriteStatus(movieId, isFavorite)
    }

    override suspend fun toggleWatchedStatus(movieId: Int, isWatched: Boolean) {
        localDataSource.toggleWatchedStatus(movieId, isWatched)
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovies().map { entities ->
            mapper.entityListToDomainList(entities)
        }
    }

    override fun getWatchedMovies(): Flow<List<Movie>> {
        return localDataSource.getWatchedMovies().map { entities ->
            mapper.entityListToDomainList(entities)
        }
    }

    override suspend fun addMovie(title: String, year: Int): Movie? {
        return try {
            val newMovieDto = remoteDataSource.addMovie(title, year)
            val entity = mapper.dtoToEntity(newMovieDto)
            localDataSource.saveMovie(entity)
            mapper.entityToDomain(entity)
        } catch (e: Exception) {
            null
        }
    }
}