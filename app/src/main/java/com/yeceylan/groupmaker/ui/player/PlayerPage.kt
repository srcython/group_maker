package com.yeceylan.groupmaker.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlayerPage(
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val users by playerViewModel.users.collectAsState()
    val selectedUsers by playerViewModel.selectedUsers.collectAsState()
    var showUserDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Seçili Oyuncular")
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(selectedUsers) { user ->
                Text(text = user.name)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* Oyuncu Ekle İşlemi */ }) {
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
                                Text(text = user.name)
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
}
