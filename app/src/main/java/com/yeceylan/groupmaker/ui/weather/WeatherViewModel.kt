package com.yeceylan.groupmaker.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.model.WeatherResponse
import com.yeceylan.groupmaker.domain.use_cases.weather.GetWeatherForecastUseCase
import com.yeceylan.groupmaker.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase
) : ViewModel() {

    private val _weatherInfo = MutableStateFlow<Resource<WeatherResponse>?>(null)
    val weatherInfo: StateFlow<Resource<WeatherResponse>?> = _weatherInfo

    fun fetchWeatherData(latLng: String, date: String, hour: String, language: String) {
        getWeatherForecastUseCase(latLng, date, hour, language).onEach { result ->
            _weatherInfo.value = result

            when (result) {
                is Resource.Loading -> {
                    println("Veriler yükleniyor...")
                }
                is Resource.Success -> {
                    println("Veri Başarıyla Alındı: ${result.data}")
                }
                is Resource.Error -> {
                    println("WeatherError: Hava durumu verileri alınamadı: ${result.message}")
                }
            }
        }.launchIn(viewModelScope)
    }
}
