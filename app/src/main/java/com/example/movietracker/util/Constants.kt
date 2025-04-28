package com.example.movietracker.util

/**
 * Constants used throughout the application.
 *
 * This class provides a centralized place for all constants and configuration values.
 */
object Constants {
    /**
     * Base URL for API calls.
     * 10.0.2.2 is the special IP for Android emulator to reach localhost on host machine.
     */
    const val BASE_URL = "http://10.0.2.2:3000/"

    /**
     * Whether the app is in debug mode.
     */
    const val IS_DEBUG = true

    /**
     * Timeout for network connections in seconds.
     */
    const val NETWORK_TIMEOUT = 30L
}