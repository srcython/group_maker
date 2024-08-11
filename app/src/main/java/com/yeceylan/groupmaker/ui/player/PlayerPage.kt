package com.yeceylan.groupmaker.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.ui.components.SelectedPlayersGrid

@Composable
fun PlayerPage(
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val usersState by playerViewModel.filteredUsers.collectAsState()
    val selectedUsers by playerViewModel.selectedUsers.collectAsState()
    var showUserDialog by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Seçili Oyuncular")

        when (usersState) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is Resource.Error -> {
                Text(
                    text = usersState.message ?: "Bir hata oluştu",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is Resource.Success -> {
                SelectedPlayersGrid(
                    modifier = Modifier.weight(1f),
                    selectedPersons = selectedUsers,
                    setSelectedPersons = { updatedList ->
                        playerViewModel.updateSelectedUsers(updatedList)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showAddPlayerDialog = true },
                colors = ButtonDefaults.buttonColors(Color.Blue),
            ) {
                Text(
                    text = "Oyuncu Ekle",
                    color = Color.White
                )
            }
            Button(
                onClick = { showUserDialog = true },
                colors = ButtonDefaults.buttonColors(Color.Blue),
            ) {
                Text(
                    text = "Oyuncu Çağır",
                    color = Color.White,
                )
            }
        }
    }

    if (showUserDialog) {
        Dialog(onDismissRequest = { showUserDialog = false }) {
            Surface(shape = MaterialTheme.shapes.medium, elevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Kullanıcıları Seçin")

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            playerViewModel.searchUsers(query)
                        },
                        label = { Text(text = "Ara") }
                    )

                    when (usersState) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                        is Resource.Error -> {
                            Text(
                                text = usersState.message ?: "Bir hata oluştu",
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        is Resource.Success -> {
                            LazyColumn(modifier = Modifier.height(300.dp)) {
                                items(usersState.data!!) { user ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = selectedUsers.contains(user),
                                            onCheckedChange = { checked ->
                                                if (checked) {
                                                    playerViewModel.addUser(user)
                                                } else {
                                                    playerViewModel.removeUser(user)
                                                }
                                            }
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = "${user.firstName} ${user.surname}")
                                            Text(text = user.userName)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_star),
                                                contentDescription = "Star"
                                            )
                                            Text(text = user.point.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showUserDialog = false },
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                    ) {
                        Text(
                            text = "Tamam",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

    if (showAddPlayerDialog) {
        AddPlayerDialog(
            onDismiss = { showAddPlayerDialog = false },
            onAddPlayer = { user ->
                playerViewModel.addUserToFirestore(user)
                showAddPlayerDialog = false
            }
        )
    }
}

@Composable
fun SelectedPlayersGrid(modifier: Modifier, selectedPersons: List<User>, setSelectedPersons: (List<User>) -> Unit) {
    if (selectedPersons.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            modifier = modifier
                .heightIn(max = 400.dp)
        ) {
            items(selectedPersons) { person ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = person.photoUrl?.toIntOrNull() ?: R.drawable.ic_launcher_background),
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
                        text = person.userName,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp)
                    )

                }
            }
        }
    }
}

@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onAddPlayer: (User) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var point by remember { mutableStateOf(0) }
    var firstname by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 8.dp) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Yeni Oyuncu Ekle")
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") }
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "İsim") }
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text(text = "Soyisim") }
                )
                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text(text = "Pozisyon") }
                )
                OutlinedTextField(
                    value = point.toString(),
                    onValueChange = { point = it.toIntOrNull() ?: 0 },
                    label = { Text(text = "Puan") }
                )
                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = { Text(text = "Photo Resource ID") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                    ) {
                        Text(
                            text = "İptal",
                            color = Color.White,
                        )
                    }
                    Button(
                        onClick = {
                            val newUser = User(
                                email = email,
                                userName = name,
                                surname = surname,
                                position = position,
                                point = point,
                                firstName = firstname
                            )
                            onAddPlayer(newUser)
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                    ) {
                        Text(
                            text = "Ekle",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}


