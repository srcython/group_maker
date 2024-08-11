package com.yeceylan.groupmaker.ui.match.matchinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.weather.Condition
import com.yeceylan.groupmaker.domain.model.weather.Forecast
import com.yeceylan.groupmaker.domain.model.weather.ForecastDay
import com.yeceylan.groupmaker.domain.model.weather.Hour
import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import com.yeceylan.groupmaker.domain.model.weather.WeatherType
import com.yeceylan.groupmaker.ui.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MatchInfoScreen(
    navController: NavController,
    match: Match,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherResource by viewModel.weatherInfo.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWeatherDataForMatch(match)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp)
    ) {
        MatchInfoContent(match, MatchInfoViewModel())

        Spacer(modifier = Modifier.height(10.dp))

        WeatherInfoContent(weatherResource, match)

        Spacer(modifier = Modifier.height(10.dp))

        TeamNamesWithBackground(
            team1Name = match.firstTeamName ?: "Takım 1",
            team2Name = match.secondTeamName ?: "Takım 2",
            backgroundImage = R.drawable.img_stadium_background,
            team1Players = match.firstTeamPlayerList.map { it.firstName },  // Player isimleri
            team2Players = match.secondTeamPlayerList.map { it.firstName }
        )
        Spacer(modifier = Modifier.height(20.dp))
        IbanRow(viewModel = MatchInfoViewModel(), iban = "TR33 0006 1005 1978 6457 8413 26")

    }
}


@Composable
fun MatchInfoContent(match: Match, viewModel: MatchInfoViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1E88E5), Color(0xFF42A5F5))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Maç Bilgileri",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_home),
                    text = "${match.matchLocationTitle}"
                )
                Spacer(modifier = Modifier.height(4.dp))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_calendar),
                    text = "${match.matchDate}"
                )
                Spacer(modifier = Modifier.height(4.dp))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_clock),
                    text = "${match.matchTime}"
                )
                Spacer(modifier = Modifier.height(4.dp))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_maps),
                    text = "${match.matchLocation}",
                    modifier = Modifier.clickable {
                        match.matchLocation?.let { viewModel.openMapForDirections(context, it) }
                    }
                )
            }
        }
    }
}

@Composable
fun MatchInfoRow(iconPainter: Painter, text: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, color = Color.White)
    }
}


@Composable
fun MatchInfoRow(iconPainter: Painter, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, color = Color.White)
    }
}


@Composable
fun WeatherInfoContent(weatherResource: Resource<WeatherResponse>?, match: Match) {
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
                val dateParsed = inputDateFormat.parse(match.matchDate)
                val currentDate = Calendar.getInstance().time
                val diff = dateParsed.time - currentDate.time
                val diffDays = diff / (1000 * 60 * 60 * 24)

                if (forecastDay.hour.isNotEmpty() && diffDays <= 14) {
                    val currentHourWeather = forecastDay.hour[0]
                    val conditionText = currentHourWeather.condition.text
                    val weatherIconResId =
                        WeatherType.weatherIconMap[conditionText] ?: R.drawable.ic_moon

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF102840), Color(0xFF1B3B5A))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Maç Saati İçin Hava Durumu",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Image(
                                    painter = painterResource(id = weatherIconResId),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Durum: $conditionText",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Sıcaklık: ${currentHourWeather.temp_c} °C",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterHorizontally) // Metin rengi beyaz
                                )
                                Text(
                                    text = "Yağmur İhtimali: ${currentHourWeather.precip_mm} mm",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterHorizontally) // Metin rengi beyaz
                                )
                            }
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
fun TeamNamesWithBackground(
    team1Name: String,
    team2Name: String,
    team1Players: List<String>,
    team2Players: List<String>,
    backgroundImage: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = team1Name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    team1Players.forEach { player ->
                        Text(
                            text = player,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_versus),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(90.dp)
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = team2Name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    team2Players.forEach { player ->
                        Text(
                            text = player,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IbanRow(viewModel: MatchInfoViewModel, iban: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                viewModel.copyIbanToClipboard(context, iban)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "IBAN: ", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)

        Icon(
            painter = painterResource(id = R.drawable.ic_copy),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = iban, fontSize = 16.sp, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun MatchInfoContentPreview() {
    val match = Match(
        id = "1",
        matchLocationTitle = "Stadyum",
        matchLocation = "İstanbul, Türkiyedf vTürkiyedf vTürkiyedf TürkiyedfTürkiyedf",
        matchDate = "01-01-2024",
        matchTime = "18:00",
        firstTeamName = "Takım A",
        secondTeamName = "Takım B",
        type = "Lig Maçı",
        latLng = LatLng(41.0082, 28.9784),
        isActive = true,
        maxPlayer = 22
    )

    MatchInfoContent(match = match, viewModel = MatchInfoViewModel())
}

@Preview(showBackground = true)
@Composable
fun WeatherInfoContentPreview() {
    val match = Match(
        id = "1",
        matchLocationTitle = "Stadyum",
        matchLocation = "İstanbul, Türkiye",
        matchDate = "01-01-2024",
        matchTime = "18:00",
        firstTeamName = "Takım A",
        secondTeamName = "Takım B",
        type = "Lig Maçı",
        latLng = LatLng(41.0082, 28.9784),
        isActive = true,
        maxPlayer = 22
    )

    val fakeWeatherResponse = WeatherResponse(
        forecast = Forecast(
            forecastday = listOf(
                ForecastDay(
                    date = "2024-01-01",
                    hour = listOf(
                        Hour(
                            time = "2024-01-01 18:00",
                            temp_c = 15.0,
                            condition = Condition(
                                text = "Sunny"
                            ),
                            precip_mm = 0.0
                        )
                    )
                )
            )
        )
    )
    val weatherResource = Resource.Success(fakeWeatherResponse)

    WeatherInfoContent(weatherResource = weatherResource, match = match)
}

@Preview(showBackground = true)
@Composable
fun TeamNamesWithBackgroundPreview() {
    TeamNamesWithBackground(
        team1Name = "Takım 1",
        team2Name = "Takım 2",
        backgroundImage = R.drawable.img_stadium_background,
        team1Players = listOf(
            "Oyuncu 1",
            "Oyuncu 2",
            "Oyuncu 1",
            "Oyuncu 2",
            "Oyuncu 1",
            "Oyuncu 2",
            "Oyuncu 1",
            "Oyuncu 2",
            "Oyuncu 1",
            "Oyuncu 2",
            "Oyuncu 1"
        ),
        team2Players = listOf("Oyuncu 3", "Oyuncu 4")
    )
}

@Preview(showBackground = true)
@Composable
fun IbanRowPreview() {
    val viewModel: MatchInfoViewModel = viewModel()
    IbanRow(viewModel = viewModel, iban = "TR00 0000 0000 0000 0000 0000")
}
