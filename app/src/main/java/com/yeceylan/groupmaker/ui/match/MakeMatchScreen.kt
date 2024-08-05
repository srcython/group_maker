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
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.components.ChangeTeamNamesDialog
import com.yeceylan.groupmaker.ui.components.MatchDateInputField
import com.yeceylan.groupmaker.ui.components.MatchLocationInputField
import com.yeceylan.groupmaker.ui.components.MatchTimeInputField
import com.yeceylan.groupmaker.ui.components.PlayerCountDialog
import com.yeceylan.groupmaker.ui.components.PlayerSelectionSection
import com.yeceylan.groupmaker.ui.components.SelectedPlayersGrid
import com.yeceylan.groupmaker.ui.location.LocationViewModel

data class Person(
    val name: String,
    val surname: String,
    val photoResId: Int,
    val position: String,
    val point: Int
)

@Composable
fun MakeMatchScreen(
    teamSize:Int,
    navController: NavController,
    viewModel: LocationViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    var team1Name by remember { mutableStateOf("Takım 1") }
    var team2Name by remember { mutableStateOf("Takım 2") }
    val persons = remember {
        listOf(
            Person("Jane", "Smith", R.drawable.ic_launcher_background, "Forward", 10),
            Person("Bob", "Johnson", R.drawable.ic_launcher_background, "Midfielder", 8),
            Person("Alice", "Brown", R.drawable.ic_launcher_background, "Defender", 6),
            Person("Charlie", "Davis", R.drawable.ic_launcher_background, "Goalkeeper", 9),
            Person("Eve", "Wilson", R.drawable.ic_launcher_background, "Midfielder", 7),
            Person("Frank", "Miller", R.drawable.ic_launcher_background, "Defender", 5),
            Person("Grace", "Lee", R.drawable.ic_launcher_background, "Forward", 9),
            Person("Hank", "Martinez", R.drawable.ic_launcher_background, "Midfielder", 7),
            Person("Iasvy", "Clark", R.drawable.ic_launcher_background, "Defender", 6),
            Person("Jaack", "Lewis", R.drawable.ic_launcher_background, "Goalkeeper", 8),
            Person("Kaaren", "Walker", R.drawable.ic_launcher_background, "Forward", 10),
            Person("Jaane", "Smith", R.drawable.ic_launcher_background, "Forward", 10),
            Person("Boasb", "Johnson", R.drawable.ic_launcher_background, "Midfielder", 8),
            Person("Alisace", "Brown", R.drawable.ic_launcher_background, "Defender", 6),
            Person("Chasarlie", "Davis", R.drawable.ic_launcher_background, "Goalkeeper", 9),
            Person("saEve", "Wilson", R.drawable.ic_launcher_background, "Midfielder", 7),
            Person("Frsank", "Miller", R.drawable.ic_launcher_background, "Defender", 5),
            Person("Grssace", "Lee", R.drawable.ic_launcher_background, "Forward", 9),
            Person("Hasnk", "Martinez", R.drawable.ic_launcher_background, "Midfielder", 7),
            Person("Ivsy", "Clark", R.drawable.ic_launcher_background, "Defender", 6),
            Person("Jasck", "Lewis", R.drawable.ic_launcher_background, "Goalkeeper", 8),
            Person("Jasack", "Lewis", R.drawable.ic_launcher_background, "Goalkeeper", 8)
        )
    }

    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var selectedPersons1 by remember { mutableStateOf(listOf<Person>()) }
    var selectedPersons2 by remember { mutableStateOf(listOf<Person>()) }
    var maxPlayers by remember { mutableIntStateOf(teamSize) }
    var showPlayerCountDialog by remember { mutableStateOf(false) }
    var showChangeTeamNamesDialog by remember { mutableStateOf(false) }
    var matchLocation by remember { mutableStateOf("") }
    val selectedLocation by viewModel.selectedLocation.collectAsState(initial = null)
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

                // Konum arama bileşeni
                MatchLocationInputField(
                    label = "Maç Konumu",
                    value = matchLocation,
                    onValueChange = { matchLocation = it },
                    viewModel = viewModel
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 50.dp)
                    ) {
                        item {
                            PlayerSelectionSection(
                                teamName = team1Name,
                                selectedPersons = selectedPersons1,
                                availablePersons = persons.filter { it !in selectedPersons2 },
                                maxPlayers = maxPlayers,
                                expanded = expanded1,
                                setExpanded = { expanded1 = it },
                                setSelectedPersons = { selectedPersons1 = it }
                            )
                            SelectedPlayersGrid(selectedPersons1) { selectedPersons1 = it }
                            Spacer(modifier = Modifier.height(16.dp))
                            PlayerSelectionSection(
                                teamName = team2Name,
                                selectedPersons = selectedPersons2,
                                availablePersons = persons.filter { it !in selectedPersons1 },
                                maxPlayers = maxPlayers,
                                expanded = expanded2,
                                setExpanded = { expanded2 = it },
                                setSelectedPersons = { selectedPersons2 = it }
                            )
                            SelectedPlayersGrid(selectedPersons2) { selectedPersons2 = it }
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
                                Toast.makeText(
                                    context,
                                    "Maç başarıyla oluşturuldu!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Navigate to the next screen or perform any other actions
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp)
                    ) {
                        Text(text = "Maç Oluştur")
                    }
                }
            }
        }
    )
}
