package com.yeceylan.groupmaker.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.use_cases.weather.GetWeatherForecastUseCase
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.weather.WeatherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Success(
        val conditionText: String,
        val temperature: Double,
        val precipitation: Double,
        val weatherIconResId: Int
    ) : WeatherUiState()

    data class Error(val message: String) : WeatherUiState()
    data object NoData : WeatherUiState()
}

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
            val dateParsed = match.matchDate?.let { inputDateFormat.parse(it) }
            val formattedDate = dateParsed?.let { outputDateFormat.format(it) }

            val (hourString, minuteString) = match.matchTime!!.split(":")
            val hour = hourString.toInt()
            val minute = minuteString.toInt()
            val roundedHour = if (minute >= 30) (hour + 1) % 24 else hour

            if (formattedDate != null) {
                fetchWeatherData(
                    latLng = latLngProcessed,
                    date = formattedDate,
                    hour = roundedHour.toString(),
                    language = "tr"
                )
            }
        }
    }

    private fun fetchWeatherData(latLng: String, date: String, hour: String, language: String) {
        getWeatherForecastUseCase(latLng, date, hour, language).onEach { result ->
            _weatherInfo.value = result
        }.launchIn(viewModelScope)
    }

    fun getWeatherInfoForMatch(match: Match): WeatherUiState {
        return when (val weatherResource = _weatherInfo.value) {
            is Resource.Loading -> WeatherUiState.Loading
            is Resource.Success -> {
                val weatherInfo = weatherResource.data
                if (weatherInfo != null && weatherInfo.forecast.forecastday.isNotEmpty()) {
                    val forecastDay = weatherInfo.forecast.forecastday[0]

                    val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val dateParsed = match.matchDate?.let { inputDateFormat.parse(it) }
                    val currentDate = Calendar.getInstance().time
                    val diff = (dateParsed?.time ?: 0) - currentDate.time
                    val diffDays = diff / (1000 * 60 * 60 * 24)

                    if (forecastDay.hour.isNotEmpty() && diffDays <= 14) {
                        val currentHourWeather = forecastDay.hour[0]
                        val conditionText = currentHourWeather.condition.text
                        val weatherIconResId =
                            WeatherType.weatherIconMap[conditionText] ?: R.drawable.ic_star

                        return WeatherUiState.Success(
                            conditionText = conditionText,
                            temperature = currentHourWeather.temp_c,
                            precipitation = currentHourWeather.precip_mm,
                            weatherIconResId = weatherIconResId
                        )
                    } else {
                        WeatherUiState.NoData
                    }
                } else {
                    WeatherUiState.NoData
                }
            }

            is Resource.Error -> WeatherUiState.Error(weatherResource.message ?: "Hata")
            else -> WeatherUiState.NoData
        }
    }
}
