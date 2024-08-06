package com.yeceylan.groupmaker.ui.player

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.ui.components.SelectedPlayersGrid

@Composable
fun PlayerPage(
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val users by playerViewModel.users.collectAsState()
    val selectedUsers by playerViewModel.selectedUsers.collectAsState()
    var showUserDialog by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Seçili Oyuncular")
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(selectedUsers) { user ->
                Text(text = user.userName)
            }
        }

         SelectedPlayersGrid(selectedPersons = selectedUsers, setSelectedPersons = { updatedList ->
             playerViewModel.updateSelectedUsers(updatedList)
         })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showAddPlayerDialog = true }) {
                Text(text = "Oyuncu Ekle")
            }
            Button(onClick = { showUserDialog = true }) {
                Text(text = "Oyuncu Çağır")
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
                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(users) { user ->
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
                                Text(text = user.userName)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { showUserDialog = false }) {
                        Text(text = "Tamam")
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
                    Button(onClick = onDismiss) {
                        Text(text = "İptal")
                    }
                    Button(onClick = {
                        val newUser = User(
                            email = email,
                            userName = name,
                            surname = surname,
                            position = position,
                            point = point,
                            firstName = firstname
                        )
                        onAddPlayer(newUser)
                    }) {
                        Text(text = "Ekle")
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedPlayersGrid(selectedPersons: List<User>, setSelectedPersons: (List<User>) -> Unit) {
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
}
