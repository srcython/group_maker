package com.yeceylan.groupmaker.ui.match.matchinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.DeadObjectException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.weather.Hour
import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import com.yeceylan.groupmaker.domain.model.weather.WeatherType
import com.yeceylan.groupmaker.domain.use_cases.AddOldMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.GetActiveMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.UpdateMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.GetCurrentUserUidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MatchInfoViewModel @Inject constructor(
    private val addOldMatchUseCase: AddOldMatchUseCase,
    private val getActiveMatchUseCase: GetActiveMatchUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase,
    savedStateHandle: SavedStateHandle

) : ViewModel() {
    private val matchType: String = savedStateHandle["matchType"] ?: ""

    fun copyIbanToClipboard(context: Context, iban: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("IBAN", iban)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "IBAN kopyalandı", Toast.LENGTH_SHORT).show()
    }

    fun openMapForDirections(context: Context, address: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Harita uygulaması bulunamadı", Toast.LENGTH_SHORT).show()
        }
    }

    fun processWeatherData(
        weatherResource: Resource<WeatherResponse>?,
        matchDate: String
    ): Pair<Hour?, Int?>? {
        if (weatherResource is Resource.Success) {
            val weatherInfo = weatherResource.data
            if (weatherInfo != null && weatherInfo.forecast.forecastday.isNotEmpty()) {
                val forecastDay = weatherInfo.forecast.forecastday[0]

                val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val dateParsed = inputDateFormat.parse(matchDate)
                val currentDate = Calendar.getInstance().time
                val diff = dateParsed.time - currentDate.time
                val diffDays = diff / (1000 * 60 * 60 * 24)

                if (forecastDay.hour.isNotEmpty() && diffDays <= 14) {
                    val currentHourWeather = forecastDay.hour[0]
                    val conditionText = currentHourWeather.condition.text
                    val weatherIconResId =
                        WeatherType.weatherIconMap[conditionText] ?: R.drawable.ic_star
                    return Pair(currentHourWeather, weatherIconResId)
                }
            }
        }
        return null
    }

    fun finishMatch() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserUidUseCase()
                val activeMatch = getActiveMatchUseCase(userId)

                if (activeMatch != null) {
                    val updatedMatch = activeMatch.copy(isActive = false)
                    updateMatchUseCase(userId, updatedMatch) // This should now correctly update the match
                    addOldMatchUseCase(updatedMatch)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Exception occurred", e)
            }
        }
    }

}

