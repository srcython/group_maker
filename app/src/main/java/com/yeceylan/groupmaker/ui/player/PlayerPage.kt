package com.yeceylan.groupmaker.ui.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.user.User
import com.yeceylan.groupmaker.ui.components.button.BackButton
import com.yeceylan.groupmaker.ui.theme.Dimen

@Composable
fun PlayerPage(
    playerViewModel: PlayerViewModel = hiltViewModel(),
    navController: NavController
) {
    val usersState by playerViewModel.filteredUsers.collectAsState()
    val selectedUsers by playerViewModel.selectedUsers.collectAsState()
    var showUserDialog by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(top = 60.dp, start = 20.dp)) {
            BackButton {
                navController.popBackStack()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimen.spacing_m1)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = stringResource(R.string.selected_players))

                when (usersState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    is Resource.Error -> {
                        Text(
                            text = usersState.message ?: stringResource(R.string.an_error_occured),
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    is Resource.Success -> {
                        if (selectedUsers.isEmpty()) {
                            Text(
                                text = stringResource(R.string.no_players_selected),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            SelectedPlayersGrid(
                                modifier = Modifier.weight(20f),
                                selectedPersons = selectedUsers,
                                setSelectedPersons = { updatedList ->
                                    playerViewModel.updateSelectedUsers(updatedList)
                                }
                            )
                        }
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
                            text = stringResource(R.string.add_player),
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = { showUserDialog = true },
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                    ) {
                        Text(
                            text = stringResource(R.string.call_player),
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

    if (showUserDialog) {
        Dialog(onDismissRequest = { showUserDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color.White,
                border = BorderStroke(Dimen.spacing_xxxs, Color.Black),
            ) {
                Column(
                    modifier = Modifier
                        .padding(Dimen.spacing_m1)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.choose_players))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            playerViewModel.searchUsers(query)
                        },
                        label = { Text(text = stringResource(R.string.search)) }
                    )

                    when (usersState) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                        is Resource.Error -> {
                            Text(
                                text = usersState.message ?: stringResource(R.string.an_error_occured),
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        is Resource.Success -> {
                            LazyColumn(modifier = Modifier.height(Dimen.spacing_xxxl * 5)) {
                                items(usersState.data!!) { user ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
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
                                                contentDescription = stringResource(R.string.star),
                                            )
                                            Text(text = user.point.toString())
                                        }
                                    }
                                    Divider()
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimen.spacing_xs))
                    Button(
                        onClick = { showUserDialog = false },
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                    ) {
                        Text(
                            text = stringResource(R.string.okey),
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
            contentPadding = PaddingValues(Dimen.spacing_xs),
            modifier = modifier
                .heightIn(Dimen.spacing_xxxl * 6)
        ) {
            items(selectedPersons) { person ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box {
                        AsyncImage(
                            model = person.photoUrl,
                            placeholder = painterResource(id = R.drawable.ic_clock),
                            error = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier
                                .size(Dimen.spacing_xxxl)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                        Box(
                            modifier = Modifier
                                .size(Dimen.spacing_m1)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    setSelectedPersons(selectedPersons - person)
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_remove),
                                contentDescription = stringResource(R.string.remove),
                                modifier = Modifier.size(Dimen.spacing_m1)
                            )
                        }
                    }
                    Text(
                        text = person.userName,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = Dimen.spacing_xxs)
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
    var userName by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var point by remember { mutableIntStateOf(0) }
    var firstname by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            border = BorderStroke(Dimen.spacing_xxxs, Color.Black),
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimen.spacing_m1)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.add_new_player))

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.email)
                )

                CustomTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = stringResource(R.string.username)
                )

                CustomTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = stringResource(R.string.name)
                )

                CustomTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = stringResource(R.string.lastname)
                )

                CustomTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = stringResource(R.string.position)
                )

                CustomTextField(
                    value = point.toString(),
                    onValueChange = { point = it.toIntOrNull() ?: 0 },
                    label = stringResource(R.string.point)
                )

                Spacer(modifier = Modifier.height(Dimen.spacing_xs))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = Color.White,
                        )
                    }
                    Button(
                        onClick = {
                            val newUser = User(
                                email = email,
                                userName = userName,
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
                            text = stringResource(R.string.add),
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black
        ),
        modifier = modifier
    )
}



