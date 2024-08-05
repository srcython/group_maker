package com.yeceylan.groupmaker.ui.match.matchinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.MatchInfo
import com.yeceylan.groupmaker.domain.model.WeatherResponse
import com.yeceylan.groupmaker.ui.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object WeatherIcons {
    val weatherIconMap = mapOf(
        // Güneşli Hava Koşulları
        "Açık" to R.drawable.ic_star,
        "Güneşli" to R.drawable.day,
        "Diğer" to R.drawable.day
    )
}

@Composable
fun MatchInfoScreen(
    navController: NavController,
    matchInfo: MatchInfo,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherResource by viewModel.weatherInfo.collectAsState()

    LaunchedEffect(Unit) {
        val regex = Regex("lat/lng: \\(([^)]+)\\)")
        val latLngProcessed = regex.find(matchInfo.latLng.toString())?.groupValues?.get(1) ?: ""

        val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateParsed = inputDateFormat.parse(matchInfo.matchDate)
        val formattedDate = outputDateFormat.format(dateParsed)

        // Saati ve dakikayı ayırma
        val (hourString, minuteString) = matchInfo.matchTime.split(":")
        val hour = hourString.toInt()
        val minute = minuteString.toInt()

        // Dakikayı kullanarak saati yuvarlama
        val roundedHour = if (minute >= 30) hour + 1 else hour

        println("Düzenlenmiş Enlem, Boylam: $latLngProcessed")
        println("Tarih: $formattedDate")
        println("Saat: $roundedHour")

        viewModel.fetchWeatherData(
            latLng = latLngProcessed,
            date = formattedDate,
            hour = roundedHour.toString(),
            language = "tr"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MatchInfoContent(matchInfo)

        Spacer(modifier = Modifier.height(20.dp))

        WeatherInfoContent(weatherResource, matchInfo)

        Spacer(modifier = Modifier.height(20.dp))

        TeamNamesWithBackground(
            team1Name = matchInfo.team1Name,
            team2Name = matchInfo.team2Name,
            backgroundImage = R.drawable.img_stadium_background
        )
    }
}

@Composable
fun MatchInfoContent(matchInfo: MatchInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(0xFFE3F2FD)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Maç Bilgileri",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Yer Adı: ${matchInfo.matchLocation}", fontSize = 16.sp)
            Text(text = "Tarih: ${matchInfo.matchDate}", fontSize = 16.sp)
            Text(text = "Saat: ${matchInfo.matchTime}", fontSize = 16.sp)
            Text(text = "Adres: ${matchInfo.address}", fontSize = 16.sp)
        }
    }
}

@Composable
fun WeatherInfoContent(weatherResource: Resource<WeatherResponse>?, matchInfo: MatchInfo) {
    when (weatherResource) {
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .padding(16.dp),
                color = Color(0xFF388E3C)
            )
        }
        is Resource.Success -> {
            val weatherInfo = weatherResource.data
            if (weatherInfo != null && weatherInfo.forecast.forecastday.isNotEmpty()) {
                val forecastDay = weatherInfo.forecast.forecastday[0]

                val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val dateParsed = inputDateFormat.parse(matchInfo.matchDate)
                val currentDate = Calendar.getInstance().time
                val diff = dateParsed.time - currentDate.time
                val diffDays = diff / (1000 * 60 * 60 * 24)

                if (forecastDay.hour.isNotEmpty() && diffDays <= 14) {
                    val currentHourWeather = forecastDay.hour[0]
                    val conditionText = currentHourWeather.condition.text
                    val weatherIconResId = WeatherIcons.weatherIconMap[conditionText] ?: R.drawable.day

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color(0xFFC8E6C9)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Maç Saati İçin Hava Durumu",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF388E3C)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = painterResource(id = weatherIconResId),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Durum: $conditionText",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Sıcaklık: ${currentHourWeather.temp_c} °C",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Yağmur İhtimali: ${currentHourWeather.precip_mm} mm",
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Hava durumu saati mevcut değil. Lütfen daha sonra tekrar deneyin.",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        is Resource.Error -> {
            Text(
                text = "Hata: ${weatherResource.message}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        else -> {
            Text(
                text = "Hava durumu bilgisi mevcut değil. Lütfen daha sonra tekrar deneyin.",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TeamNamesWithBackground(team1Name: String, team2Name: String, backgroundImage: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Text(
                text = team1Name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "vs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = team2Name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
