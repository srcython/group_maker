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

        try {
            val response = apiService.getWeatherForecast(
                apiKey = "88ccbf311c5e4e32bb0135032240408",
                latLng = latLng,
                date = date,
                hour = hour,
                language = language
            )
            emit(Resource.Success(response))

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu"))
        }
    }
}
