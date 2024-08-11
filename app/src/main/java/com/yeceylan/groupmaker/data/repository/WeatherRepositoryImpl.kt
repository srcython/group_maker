package com.yeceylan.groupmaker.data.repository

import android.util.Log
import com.yeceylan.groupmaker.domain.model.WeatherApiService
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import com.yeceylan.groupmaker.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRepository {
    override fun getWeatherForecast(
        latLng: String,
        date: String,
        hour: String,
        language: String
    ): Flow<Resource<WeatherResponse>> = flow {
        emit(Resource.Loading())

        // API'ye gönderilen veriler
        Log.d("WeatherRepository", "API Request - LatLng: $latLng, Date: $date, Hour: $hour, Language: $language")

        try {
            val response = apiService.getWeatherForecast(
                apiKey = "88ccbf311c5e4e32bb0135032240408",
                latLng = latLng,
                date = date,
                hour = hour,
                language = language
            )
            emit(Resource.Success(response))

            Log.d("WeatherRepository", "API Response Success: $response")
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu"))

            Log.e("WeatherRepository", "API Response Error: ${e.message}")
        }
    }
}
