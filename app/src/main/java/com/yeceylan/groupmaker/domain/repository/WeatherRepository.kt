package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForecast(
        latLng: String,
        date: String,
        hour: String,
        language: String
    ): Flow<Resource<WeatherResponse>>
}
