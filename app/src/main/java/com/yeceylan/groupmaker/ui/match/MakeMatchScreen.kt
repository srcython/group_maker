package com.yeceylan.groupmaker.ui.match

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.MatchInfo
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.ui.components.ChangeTeamNamesDialog
import com.yeceylan.groupmaker.ui.components.MatchDateInputField
import com.yeceylan.groupmaker.ui.components.MatchLocationInputField
import com.yeceylan.groupmaker.ui.components.MatchTimeInputField
import com.yeceylan.groupmaker.ui.components.PlayerCountDialog
import com.yeceylan.groupmaker.ui.components.PlayerSelectionSection
import com.yeceylan.groupmaker.ui.components.SelectedPlayersGrid
import com.yeceylan.groupmaker.ui.location.LocationViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MakeMatchScreen(
    teamSize: Int,
    navController: NavController,
    makeMatchViewModel: MakeMatchViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    var team1Name by remember { mutableStateOf("Takım 1") }
    var team2Name by remember { mutableStateOf("Takım 2") }
    val usersResource by makeMatchViewModel.users.collectAsState()

    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var selectedPersons1 by remember { mutableStateOf(listOf<User>()) }
    var selectedPersons2 by remember { mutableStateOf(listOf<User>()) }
    var maxPlayers by remember { mutableIntStateOf(teamSize) }
    var showPlayerCountDialog by remember { mutableStateOf(false) }
    var showChangeTeamNamesDialog by remember { mutableStateOf(false) }
    var matchLocation by remember { mutableStateOf("") }
    val locationLatLng by locationViewModel.selectedLocation.collectAsState(initial = null)
    val selectedAddress by locationViewModel.selectedAddress.collectAsState(initial = "")
    var matchDate by remember { mutableStateOf("") }
    var matchTime by remember { mutableStateOf("") }

    if (showPlayerCountDialog) {
        PlayerCountDialog(maxPlayers) { maxPlayers = it; showPlayerCountDialog = false }
    }

    if (showChangeTeamNamesDialog) {
        ChangeTeamNamesDialog(
            team1Name = team1Name,
            team2Name = team2Name,
            onTeamNamesChanged = { newTeam1Name, newTeam2Name ->
                team1Name = newTeam1Name
                team2Name = newTeam2Name
                showChangeTeamNamesDialog = false
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = "Maç Yeri Ve Zamanı",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(5.dp))

                MatchLocationInputField(
                    label = "Maç konumu giriniz",
                    value = matchLocation,
                    onValueChange = { matchLocation = it },
                    viewModel = locationViewModel
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MatchDateInputField(
                        label = "Maç Tarihi: ",
                        value = matchDate,
                        onValueChange = { matchDate = it },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    MatchTimeInputField(
                        label = "Maç Saati: ",
                        value = matchTime,
                        onValueChange = { matchTime = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Takım Oluştur",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = { showPlayerCountDialog = true },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "Takımlar kaç kişilik?", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { showChangeTeamNamesDialog = true },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "Takım Adlarını Değiştir", fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    when (usersResource) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                        is Resource.Success -> {
                            val users = usersResource.data ?: emptyList()

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 50.dp)
                            ) {
                                item {
                                    PlayerSelectionSection(
                                        teamName = team1Name,
                                        selectedUsers = selectedPersons1,
                                        availableUsers = users.filter { it !in selectedPersons2 },
                                        maxPlayers = maxPlayers,
                                        expanded = expanded1,
                                        setExpanded = { expanded1 = it },
                                        setSelectedPersons = { selectedPersons1 = it }
                                    )
                                    SelectedPlayersGrid(selectedPersons1) { selectedPersons1 = it }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    PlayerSelectionSection(
                                        teamName = team2Name,
                                        selectedUsers = selectedPersons2,
                                        availableUsers = users.filter { it !in selectedPersons1 },
                                        maxPlayers = maxPlayers,
                                        expanded = expanded2,
                                        setExpanded = { expanded2 = it },
                                        setSelectedPersons = { selectedPersons2 = it }
                                    )
                                    SelectedPlayersGrid(selectedPersons2) { selectedPersons2 = it }
                                }
                            }
                        }
                        is Resource.Error -> {
                            Text(
                                text = "Hata: ${usersResource.message}",
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val context = navController.context

                            if (matchLocation.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Lütfen maç konumunu girin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (matchDate.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Lütfen maç tarihini seçin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (matchTime.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Lütfen maç saatini seçin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (selectedPersons1.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "$team1Name için oyuncu seçin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (selectedPersons2.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "$team2Name için oyuncu seçin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val matchInfo = MatchInfo(
                                    team1Name = team1Name,
                                    team2Name = team2Name,
                                    matchLocation = matchLocation,
                                    matchDate = matchDate,
                                    matchTime = matchTime,
                                    latLng = locationLatLng,
                                    address = selectedAddress ?: "Bilinmeyen Addres"
                                )

                                val matchInfoJson = Gson().toJson(matchInfo)
                                val encodedJson = URLEncoder.encode(
                                    matchInfoJson,
                                    StandardCharsets.UTF_8.toString()
                                ).replace("+", "%20")
                                navController.navigate("matchInfo/$encodedJson")
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(5.dp)
                    ) {
                        Text(text = "Maç Oluştur")
                    }
                }
            }
        }
    )
}
