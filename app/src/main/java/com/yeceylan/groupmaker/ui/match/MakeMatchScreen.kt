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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.ui.components.*
import com.yeceylan.groupmaker.ui.components.text.MatchDateInputField
import com.yeceylan.groupmaker.ui.components.text.MatchLocationInputField
import com.yeceylan.groupmaker.ui.components.text.MatchTimeInputField
import com.yeceylan.groupmaker.ui.components.text.PlayerSelectionSection
import com.yeceylan.groupmaker.ui.location.LocationViewModel
import com.yeceylan.groupmaker.ui.theme.Dimen

@Composable
fun MakeMatchScreen(
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
                    .padding(Dimen.spacing_m1)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                Text(
                    modifier = Modifier.padding(top = Dimen.spacing_l2),
                    text = stringResource(R.string.title_match_location_and_time),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimen.font_size_m2
                )
                Spacer(modifier = Modifier.height(Dimen.spacing_xxs))

                MatchLocationInputField(
                    label = stringResource(R.string.enter_match_location),
                    value = matchLocation,
                    onValueChange = { makeMatchViewModel.setMatchLocation(it) },
                    viewModel = locationViewModel
                )

                Spacer(modifier = Modifier.height(Dimen.spacing_xs))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MatchDateInputField(
                        label = stringResource(R.string.match_date),
                        value = matchDate,
                        onValueChange = { makeMatchViewModel.setMatchDate(it) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(Dimen.spacing_xs))
                    MatchTimeInputField(
                        label = stringResource(R.string.match_time),
                        value = matchTime,
                        onValueChange = { makeMatchViewModel.setMatchTime(it) },
                        matchDate = matchDate,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(Dimen.spacing_m1))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.title_create_team),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimen.font_size_m2
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { makeMatchViewModel.togglePlayerCountDialog(true) },
                            modifier = Modifier.padding(Dimen.spacing_xs)
                        ) {
                            Text(text = stringResource(R.string.how_many_players_per_team), fontSize = Dimen.font_size_s1)
                        }
                        Spacer(modifier = Modifier.width(Dimen.spacing_m1))
                        Button(
                            onClick = { makeMatchViewModel.toggleChangeTeamNamesDialog(true) },
                            modifier = Modifier.padding(Dimen.spacing_xs)
                        ) {
                            Text(text = stringResource(R.string.edit_team_names), fontSize = Dimen.font_size_s1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimen.spacing_m1))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .border(
                            Dimen.spacing_xxxs,
                            Color.Gray,
                            RoundedCornerShape(Dimen.spacing_xs)
                        )
                ) {
                    when (userList) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        is Resource.Success -> {
                            val users = userList.data ?: emptyList()
                            if (users.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.no_players_added),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                return@Scaffold
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = Dimen.spacing_s1,
                                        start = Dimen.spacing_s1,
                                        end = Dimen.spacing_s1,
                                        bottom = Dimen.spacing_xxl
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
                                    Spacer(modifier = Modifier.height(Dimen.spacing_m1))
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
                                text = stringResource(R.string.players_could_not_be_loaded),
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
                                    context.getString(R.string.please_enter_match_location),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (matchDate.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.please_select_match_date),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (matchTime.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.please_select_match_time),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (selectedPersons1.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.please_select_first_team_players),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (selectedPersons2.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.please_select_second_team_players),
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
                            .padding(Dimen.spacing_xxs)
                    ) {
                        Text(text = stringResource(R.string.create_match))
                    }

                }
            }
        }

    )
}
