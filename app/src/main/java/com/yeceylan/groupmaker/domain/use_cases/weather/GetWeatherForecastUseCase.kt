package com.yeceylan.groupmaker.domain.use_cases.weather

import com.yeceylan.groupmaker.domain.model.WeatherResponse
import com.yeceylan.groupmaker.domain.repository.WeatherRepository
import com.yeceylan.groupmaker.core.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(
        latLng: String,
        date: String,
        hour: String,
        language: String
    ): Flow<Resource<WeatherResponse>> {
        return repository.getWeatherForecast(latLng, date, hour, language)
    }
}
