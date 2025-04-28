package com.example.movietracker.data.api

import com.example.movietracker.data.api.service.MovieApiService
import com.example.movietracker.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Utility class for creating Retrofit instances.
 *
 * This class provides a centralized way to create and configure Retrofit.
 * It follows the Builder pattern.
 */
object RetrofitBuilder {

    /**
     * Create a Retrofit instance for the movie API.
     *
     * @return A configured Retrofit instance.
     */
    fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Create an OkHttpClient with logging and timeouts.
     *
     * @return A configured OkHttpClient.
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (Constants.IS_DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Create a MovieApiService instance.
     *
     * @return A configured MovieApiService.
     */
    fun createMovieApiService(): MovieApiService {
        return createRetrofit().create(MovieApiService::class.java)
    }
}