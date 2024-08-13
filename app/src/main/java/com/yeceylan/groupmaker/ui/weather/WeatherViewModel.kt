package com.yeceylan.groupmaker.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.use_cases.weather.GetWeatherForecastUseCase
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import com.yeceylan.groupmaker.domain.model.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase
) : ViewModel() {

    private val _weatherInfo = MutableStateFlow<Resource<WeatherResponse>?>(null)
    val weatherInfo: StateFlow<Resource<WeatherResponse>?> = _weatherInfo

    fun fetchWeatherDataForMatch(match: Match) {
        viewModelScope.launch {
            val regex = Regex("lat/lng: \\(([^)]+)\\)")
            val latLngProcessed = regex.find(match.latLng.toString())?.groupValues?.get(1) ?: ""

            val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateParsed = inputDateFormat.parse(match.matchDate)
            val formattedDate = outputDateFormat.format(dateParsed)


            val (hourString, minuteString) = match.matchTime!!.split(":")
            val hour = hourString.toInt()
            val minute = minuteString.toInt()
            val roundedHour = if (minute >= 30) (hour + 1) % 24 else hour

            fetchWeatherData(
                latLng = latLngProcessed,
                date = formattedDate,
                hour = roundedHour.toString(),
                language = "tr"
            )
        }
    }

    private fun fetchWeatherData(latLng: String, date: String, hour: String, language: String) {
        getWeatherForecastUseCase(latLng, date, hour, language).onEach { result ->
            _weatherInfo.value = result

            when (result) {
                is Resource.Loading -> println("Veriler yükleniyor...")
                is Resource.Success -> println("Veri Başarıyla Alındı: ${result.data}")
                is Resource.Error -> println("WeatherError: Hava durumu verileri alınamadı: ${result.message}")
            }
        }.launchIn(viewModelScope)
    }
}