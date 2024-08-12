package com.yeceylan.groupmaker.ui.match

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.ui.components.*
import com.yeceylan.groupmaker.ui.location.LocationViewModel

@Composable
fun MakeMatchScreen(
    sportTitle:String,
    teamSize: Int,
    navController: NavController,
    makeMatchViewModel: MakeMatchViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val team1Name by makeMatchViewModel.team1Name.collectAsState()
    val team2Name by makeMatchViewModel.team2Name.collectAsState()
    val userList by makeMatchViewModel.users.collectAsState()
    val activeMatch by makeMatchViewModel.activeMatch.collectAsState(initial = null)
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    val selectedPersons1 by makeMatchViewModel.selectedPersons1.collectAsState()
    val selectedPersons2 by makeMatchViewModel.selectedPersons2.collectAsState()
    var maxPlayers by remember { mutableIntStateOf(teamSize) }
    val showPlayerCountDialog by makeMatchViewModel.showPlayerCountDialog.collectAsState()
    val showChangeTeamNamesDialog by makeMatchViewModel.showChangeTeamNamesDialog.collectAsState()
    val matchLocation by makeMatchViewModel.matchLocation.collectAsState()
    val locationLatLng by locationViewModel.selectedLocation.collectAsState(initial = null)
    val selectedAddress by locationViewModel.selectedAddress.collectAsState(initial = "")
    val matchDate by makeMatchViewModel.matchDate.collectAsState()
    val matchTime by makeMatchViewModel.matchTime.collectAsState()

    if (showPlayerCountDialog) {
        PlayerCountDialog(maxPlayers) {
            maxPlayers = it; makeMatchViewModel.togglePlayerCountDialog(
            false
        )
        }
    }

    if (showChangeTeamNamesDialog) {
        ChangeTeamNamesDialog(
            team1Name = team1Name,
            team2Name = team2Name,
            onTeamNamesChanged = { newTeam1Name, newTeam2Name ->
                makeMatchViewModel.setTeam1Name(newTeam1Name)
                makeMatchViewModel.setTeam2Name(newTeam2Name)
                makeMatchViewModel.toggleChangeTeamNamesDialog(false)
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
                    onValueChange = { makeMatchViewModel.setMatchLocation(it) },
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
                        onValueChange = { makeMatchViewModel.setMatchDate(it) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    MatchTimeInputField(
                        label = "Maç Saati: ",
                        value = matchTime,
                        onValueChange = { makeMatchViewModel.setMatchTime(it) },
                        matchDate = matchDate,
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
                            onClick = { makeMatchViewModel.togglePlayerCountDialog(true) },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "Takımlar kaç kişilik?", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { makeMatchViewModel.toggleChangeTeamNamesDialog(true) },
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
                    when (userList) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        is Resource.Success -> {
                            val users = userList.data ?: emptyList()
                            if (users.isEmpty()) {
                                Text(
                                    text = "Herhangi bir oyuncu eklemediniz...",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                return@Scaffold
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = 10.dp,
                                        start = 10.dp,
                                        end = 10.dp,
                                        bottom = 50.dp
                                    )
                            ) {
                                item {
                                    PlayerSelectionSection(
                                        teamName = team1Name,
                                        selectedUsers = selectedPersons1,
                                        availableUsers = users.filter { it !in selectedPersons2 },
                                        maxPlayers = maxPlayers,
                                        expanded = expanded1,
                                        setExpanded = { expanded1 = it },
                                        setSelectedPersons = {
                                            makeMatchViewModel.setSelectedPersons1(
                                                it
                                            )
                                        }
                                    )
                                    SelectedPlayersGrid(selectedPersons1) {
                                        makeMatchViewModel.setSelectedPersons1(
                                            it
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    PlayerSelectionSection(
                                        teamName = team2Name,
                                        selectedUsers = selectedPersons2,
                                        availableUsers = users.filter { it !in selectedPersons1 },
                                        maxPlayers = maxPlayers,
                                        expanded = expanded2,
                                        setExpanded = { expanded2 = it },
                                        setSelectedPersons = {
                                            makeMatchViewModel.setSelectedPersons2(
                                                it
                                            )
                                        }
                                    )
                                    SelectedPlayersGrid(selectedPersons2) {
                                        makeMatchViewModel.setSelectedPersons2(
                                            it
                                        )
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            Text(
                                text = "Hata: ${userList.message}",
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
                                    "Lütfen ilk takımın oyuncularını seçin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (selectedPersons2.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Lütfen ikinci takımın oyuncularını seçin!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                selectedAddress?.let {
                                    makeMatchViewModel.updateMatchAndNavigate(
                                        navController, locationLatLng,
                                        it
                                    )
                                }
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
