package com.yeceylan.groupmaker.domain.model

import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("lang") language: String,
        @Query("q") latLng: String,
        @Query("dt") date: String,
        @Query("hour") hour: String
    ): WeatherResponse
}
