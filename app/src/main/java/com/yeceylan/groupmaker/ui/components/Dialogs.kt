package com.yeceylan.groupmaker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
fun PlayerCountDialog(maxPlayers: Int, onMaxPlayersSelected: (Int) -> Unit) {
    var tempMaxPlayers by remember { mutableStateOf(maxPlayers) }

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
