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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.model.weather.Condition
import com.yeceylan.groupmaker.domain.model.weather.Forecast
import com.yeceylan.groupmaker.domain.model.weather.ForecastDay
import com.yeceylan.groupmaker.domain.model.weather.Hour
import com.yeceylan.groupmaker.domain.model.weather.WeatherResponse
import com.yeceylan.groupmaker.ui.bottombar.BottomBarScreen
import com.yeceylan.groupmaker.ui.weather.WeatherViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import com.yeceylan.groupmaker.ui.profile.ProfileViewModel
import com.yeceylan.groupmaker.ui.theme.Dimen
import com.yeceylan.groupmaker.ui.weather.WeatherUiState

@Composable
fun MatchInfoScreen(
    navController: NavController,
    match: Match,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    matchInfoViewModel: MatchInfoViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val weatherResource by weatherViewModel.weatherInfo.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val user by profileViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeatherDataForMatch(match)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimen.spacing_xxl, start = Dimen.spacing_m1, end = Dimen.spacing_m1)
    ) {
        item {
            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Maçı Bitir", fontSize = Dimen.font_size_18)
            }
            Spacer(modifier = Modifier.height(Dimen.spacing_s1))

            MatchInfoContent(match, matchInfoViewModel)

            Spacer(modifier = Modifier.height(Dimen.spacing_s1))

            WeatherInfoContent(weatherViewModel, match)

            Spacer(modifier = Modifier.height(Dimen.spacing_s1))

            TeamNamesWithBackground(
                team1Name = match.firstTeamName ?: "Takım 1",
                team2Name = match.secondTeamName ?: "Takım 2",
                backgroundImage = R.drawable.img_stadium_background,
                team1Players = match.firstTeamPlayerList.map { it.firstName.ifEmpty { it.userName } },
                team2Players = match.secondTeamPlayerList.map { it.firstName.ifEmpty { it.userName } }
            )
            Spacer(modifier = Modifier.height(Dimen.spacing_m2))

            IbanRow(viewModel = matchInfoViewModel, user.iban ?: "Iban henüz eklenmedi")

            Spacer(modifier = Modifier.height(Dimen.spacing_m2))
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Emin misiniz?") },
            text = { Text(text = "Maçı bitirmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        matchInfoViewModel.finishMatch()
                        navController.navigate(BottomBarScreen.Home.route)
                        showDialog = false
                    }
                ) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Hayır")
                }
            }
        )
    }
}

@Composable
fun MatchInfoContent(match: Match, viewModel: MatchInfoViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = Dimen.spacing_s2,
        shape = RoundedCornerShape(Dimen.spacing_s2),
        backgroundColor = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1E88E5), Color(0xFF42A5F5))
                    )
                )
                .padding(Dimen.spacing_m1)
        ) {
            Column {
                Text(
                    text = "Maç Bilgileri",
                    fontSize = Dimen.font_size_m2,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(Dimen.spacing_xxs))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_home),
                    text = "${match.matchLocationTitle}"
                )
                Spacer(modifier = Modifier.height(Dimen.spacing_xxxs))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_calendar),
                    text = "${match.matchDate}"
                )
                Spacer(modifier = Modifier.height(Dimen.spacing_xxxs))
                MatchInfoRow(
                    iconPainter = painterResource(id = R.drawable.ic_clock),
                    text = "${match.matchTime}"
                )
                Spacer(modifier = Modifier.height(Dimen.spacing_xxxs))
                MatchInfoRowWithModifier(
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
fun MatchInfoRowWithModifier(modifier: Modifier, iconPainter: Painter, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            modifier = Modifier.size(Dimen.spacing_xl)
        )
        Spacer(modifier = Modifier.width(Dimen.spacing_xs))
        Text(text = text, fontSize = Dimen.font_size_m1, color = Color.White)
    }
}

@Composable
fun MatchInfoRow(iconPainter: Painter, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            modifier = Modifier.size(Dimen.spacing_xl)
        )
        Spacer(modifier = Modifier.width(Dimen.spacing_xs))
        Text(text = text, fontSize = Dimen.font_size_m1, color = Color.White)
    }
}

@Composable
fun WeatherInfoContent(weatherViewModel: WeatherViewModel, match: Match) {
    when (val weatherUiState = weatherViewModel.getWeatherInfoForMatch(match)) {
        is WeatherUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(Dimen.spacing_xxl)
                    .padding(Dimen.spacing_m1),
                color = Color(0xFF388E3C)
            )
        }

        is WeatherUiState.Success -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = Dimen.spacing_s2,
                shape = RoundedCornerShape(Dimen.spacing_s2),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF102840), Color(0xFF1B3B5A))
                            )
                        )
                        .padding(Dimen.spacing_m1)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Maç Saati İçin Hava Durumu",
                            fontSize = Dimen.font_size_m2,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(Dimen.spacing_m1))
                        Image(
                            painter = painterResource(id = weatherUiState.weatherIconResId),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(Dimen.spacing_xxxxl1)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(Dimen.spacing_s2))
                        Text(
                            text = "Durum: ${weatherUiState.conditionText}",
                            fontSize = Dimen.font_size_m1,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Sıcaklık: ${weatherUiState.temperature} °C",
                            fontSize = Dimen.font_size_m1,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Yağmur İhtimali: ${weatherUiState.precipitation} mm",
                            fontSize = Dimen.font_size_m1,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        is WeatherUiState.Error -> {
            Text(
                text = "Bir hata oluştu. Lütfen tekrar deneyin.",
                modifier = Modifier.padding(Dimen.spacing_m1)
            )
        }

        is WeatherUiState.NoData -> {
            Text(
                text = "Hava durumu mevcut değil. Lütfen daha yakın bir tarih seçiniz",
                modifier = Modifier.padding(Dimen.spacing_m1)
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
            .height(Dimen.spacing_xxxxl2)
            .fillMaxWidth(),
        elevation = Dimen.spacing_xxs,
        shape = RoundedCornerShape(Dimen.spacing_xs),
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
                    .padding(horizontal = Dimen.spacing_l2),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(end = Dimen.spacing_m1)
                ) {
                    Text(
                        text = team1Name,
                        fontSize = Dimen.font_size_l,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    team1Players.forEach { player ->
                        Text(
                            text = player,
                            fontSize = Dimen.font_size_m1,
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
                        .size(Dimen.spacing_xxxl2)
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = Dimen.spacing_m1)
                ) {
                    Text(
                        text = team2Name,
                        fontSize = Dimen.font_size_l,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    team2Players.forEach { player ->
                        Text(
                            text = player,
                            fontSize = Dimen.font_size_m1,
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
        Text(
            text = "IBAN: ",
            fontSize = Dimen.font_size_m1,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_copy),
            contentDescription = null,
            modifier = Modifier.size(Dimen.spacing_l)
        )
        Spacer(modifier = Modifier.width(Dimen.spacing_xxxs))
        Text(
            text = iban,
            fontSize = Dimen.font_size_m1,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MatchInfoContentPreview() {
    val match = Match(
        id = "1",
        matchLocationTitle = "Stadyum",
        matchLocation = "İstanbul, Ülker Fenerbahçe Şükrü Saracoğlu Stadyumu",
        matchDate = "01-01-2024",
        matchTime = "18:00",
        firstTeamName = "Takım A",
        secondTeamName = "Takım B",
        type = "Lig Maçı",
        latLng = LatLng(41.0082, 28.9784),
        isActive = true,
        maxPlayer = 22
    )
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
                                text = "Açık"
                            ),
                            precip_mm = 0.0
                        )
                    )
                )
            )
        )
    )
    val weatherResource = Resource.Success(fakeWeatherResponse)
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
            "Oyuncu 3"
        ),
        team2Players = listOf(
            "Oyuncu 1",
            "Oyuncu 2",
            "Oyuncu 3"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun IbanRowPreview() {
    val viewModel: MatchInfoViewModel = viewModel()
    IbanRow(viewModel = viewModel, iban = "TR00 0000 0000 0000 0000 0000")
}