package com.yeceylan.groupmaker.domain.model

import retrofit2.http.GET
import retrofit2.http.Query


data class WeatherResponse(
    val forecast: Forecast
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val hour: List<Hour>
)

data class Hour(
    val time: String,
    val temp_c: Double,
    val condition: Condition,
    val precip_mm: Double,

)

data class Condition(
    val text: String
)

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
