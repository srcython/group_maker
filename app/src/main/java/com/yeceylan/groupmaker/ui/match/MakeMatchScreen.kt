package com.yeceylan.groupmaker.ui.match

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yeceylan.groupmaker.ui.theme.GroupMakerTheme
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.auth.login.LoginViewModel
import java.util.*

data class Person(
    val name: String,
    val surname: String,
    val photoResId: Int,
    val position: String,
    val point: Int
)

@Composable
fun MakeMatchScreen(
    navController: NavController,
) {
    val focusManager = LocalFocusManager.current
    var team1Name by remember { mutableStateOf("Takım 1") }
    var team2Name by remember { mutableStateOf("Takım 2") }
    val persons = listOf(
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

    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var selectedPersons1 by remember { mutableStateOf(listOf<Person>()) }
    var selectedPersons2 by remember { mutableStateOf(listOf<Person>()) }
    var maxPlayers by remember { mutableIntStateOf(11) }
    var showPlayerCountDialog by remember { mutableStateOf(false) }
    var showChangeTeamNamesDialog by remember { mutableStateOf(false) }
    var matchLocation by remember { mutableStateOf("") }
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
            .background(Color.Blue)
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
                    label = "Maç Konumu",
                    value = matchLocation,
                    onValueChange = { matchLocation = it }
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
                        PlayerCountButton { showPlayerCountDialog = true }
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
                                team1Name,
                                selectedPersons1,
                                persons.filter { it !in selectedPersons2 },
                                maxPlayers,
                                expanded1,
                                { expanded1 = it },
                                { selectedPersons1 = it }
                            )
                            SelectedPlayersGrid(selectedPersons1) { selectedPersons1 = it }
                            Spacer(modifier = Modifier.height(16.dp))
                            PlayerSelectionSection(
                                team2Name,
                                selectedPersons2,
                                persons.filter { it !in selectedPersons1 },
                                maxPlayers,
                                expanded2,
                                { expanded2 = it },
                                { selectedPersons2 = it }
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


@Composable
fun ChangeTeamNamesDialog(
    team1Name: String,
    team2Name: String,
    onTeamNamesChanged: (String, String) -> Unit
) {
    var tempTeam1Name by remember { mutableStateOf(team1Name) }
    var tempTeam2Name by remember { mutableStateOf(team2Name) }

    AlertDialog(
        backgroundColor = Color.White,
        onDismissRequest = { onTeamNamesChanged(tempTeam1Name, tempTeam2Name) },
        title = { Text("Takım Adlarını Değiştir") },
        text = {
            Column {
                OutlinedTextField(
                    value = tempTeam1Name,
                    onValueChange = { tempTeam1Name = it },
                    label = { Text("Takım 1 Adı") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = tempTeam2Name,
                    onValueChange = { tempTeam2Name = it },
                    label = { Text("Takım 2 Adı") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onTeamNamesChanged(tempTeam1Name, tempTeam2Name) }) {
                Text("Tamam")
            }
        }
    )
}

@Composable
fun MatchLocationInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        singleLine = true
    )
}

@Composable
fun MatchDateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
            onValueChange(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable { datePickerDialog.show() }
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (value.isEmpty()) label else value,
                color = if (value.isEmpty()) Color.Gray else Color.Black
            )
        }
    }
}

@Composable
fun MatchTimeInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            val formattedTime = String.format("%02d:%02d", hour, minute)
            onValueChange(formattedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable { timePickerDialog.show() }
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = null,
                Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (value.isEmpty()) label else value,
                color = if (value.isEmpty()) Color.Gray else Color.Black
            )
        }
    }
}

@Composable
fun PlayerCountButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Kaç kişilik maç?", fontSize = 14.sp)
    }
}

@Composable
fun PlayerCountDialog(maxPlayers: Int, onMaxPlayersSelected: (Int) -> Unit) {
    var tempMaxPlayers by remember { mutableIntStateOf(maxPlayers) }

    AlertDialog(
        backgroundColor = Color.White,
        onDismissRequest = { onMaxPlayersSelected(tempMaxPlayers) },
        title = { Text("Kaç kişilik maç yapmak istiyorsunuz?") },
        text = {
            Column {
                Text("Seçim yapınız:")
                Slider(
                    value = tempMaxPlayers.toFloat(),
                    onValueChange = { tempMaxPlayers = it.toInt() },
                    valueRange = 2f..11f,
                    steps = 9
                )
                Text("Seçilen: $tempMaxPlayers kişi")
            }
        },
        confirmButton = {
            Button(onClick = { onMaxPlayersSelected(tempMaxPlayers) }) {
                Text("Tamam")
            }
        }
    )
}

@Composable
fun PlayerSelectionSection(
    teamName: String,
    selectedPersons: List<Person>,
    availablePersons: List<Person>,
    maxPlayers: Int,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    setSelectedPersons: (List<Person>) -> Unit
) {
    val context = LocalContext.current

    Text(text = "$teamName:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Seçilen Kişiler (${selectedPersons.size}/$maxPlayers)",
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Row {
            IconButton(
                onClick = {
                    val randomPlayers =
                        availablePersons.shuffled().take(maxPlayers - selectedPersons.size)
                    setSelectedPersons(selectedPersons + randomPlayers)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_random),
                    contentDescription = "İlk 11 Seç",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = {
                    setSelectedPersons(emptyList())
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bin),
                    contentDescription = "Seçimleri Kaldır",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { setExpanded(true) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedPersons.isEmpty()) "Kişi seç" else "Seçilen Kişiler: ${selectedPersons.joinToString { it.name }}",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (availablePersons.isEmpty()) {
                    DropdownMenuItem(onClick = { setExpanded(false) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Seçilecek kimse kalmadı")
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = teamName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    setExpanded(false)
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Close",
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .heightIn(max = 300.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        availablePersons.sortedBy { it.name }.forEach { person ->
                            DropdownMenuItem(
                                onClick = {
                                    if (selectedPersons.contains(person)) {
                                        setSelectedPersons(selectedPersons - person)
                                    } else if (selectedPersons.size < maxPlayers) {
                                        setSelectedPersons(selectedPersons + person)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "$teamName en fazla $maxPlayers kişi olabilir",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier.background(Color.White)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = selectedPersons.contains(person),
                                        onCheckedChange = null,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color.Green
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Image(
                                        painter = painterResource(id = person.photoResId),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "${person.name} ${person.surname}")
                                        Text(
                                            text = person.position,
                                            style = MaterialTheme.typography.body2
                                        )
                                    }
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_star),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = person.point.toString())
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun SelectedPlayersGrid(selectedPersons: List<Person>, setSelectedPersons: (List<Person>) -> Unit) {
    if (selectedPersons.isNotEmpty()) {
        Text(text = "Seçilenler:", modifier = Modifier.padding(top = 5.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .heightIn(max = 400.dp)
        ) {
            items(selectedPersons) { person ->
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = person.photoResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        setSelectedPersons(selectedPersons - person)
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_remove),
                                    contentDescription = "Remove",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = person.name,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MakeMatchScreenPreview() {
    GroupMakerTheme {
        MakeMatchScreen(navController = rememberNavController())
    }
}
