package com.example.movietracker.util

/**
 * A generic class that holds a value with its loading status.
 *
 * This is useful for UI states and also for handling API responses in a consistent way.
 * It follows the Resource pattern which helps in handling data operations and
 * propagating errors.
 *
 * @param T The type of data this resource holds.
 * @property data The data of type T (can be null).
 * @property message Error message if status is Error.
 * @property status The current status of the resource.
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        /**
         * Creates a successful resource with data.
         */
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        /**
         * Creates an error resource with an error message.
         */
        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        /**
         * Creates a loading resource.
         */
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

/**
 * Enum representing the status of a resource.
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}