package com.yeceylan.groupmaker.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.model.user.User
import com.yeceylan.groupmaker.ui.theme.Dimen

@Composable
fun PlayerSelectionSection(
    teamName: String,
    selectedUsers: List<User>,
    availableUsers: List<User>,
    maxPlayers: Int,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    setSelectedPersons: (List<User>) -> Unit
) {
    val context = LocalContext.current

    Text(text = "$teamName:", fontSize = Dimen.font_size_18, fontWeight = FontWeight.Bold)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimen.spacing_xxs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Seçilen Kişiler (${selectedUsers.size}/$maxPlayers)",
            modifier = Modifier.padding(bottom = Dimen.spacing_xxs)
        )
        Row {
            IconButton(
                onClick = {
                    val randomPlayers =
                        availableUsers.shuffled().take(maxPlayers - selectedUsers.size)
                    setSelectedPersons(selectedUsers + randomPlayers)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_random),
                    contentDescription = "İlk 11 Seç",
                    modifier = Modifier.size(Dimen.spacing_m2)
                )
            }
            IconButton(
                onClick = {
                    setSelectedPersons(emptyList())
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bin),
                    contentDescription = "Clear Selection",
                    modifier = Modifier.size(Dimen.spacing_m2)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.spacing_m1))
            .background(Color.White)
            .border(Dimen.spacing_xxxs, Color.Gray, RoundedCornerShape(Dimen.spacing_m1))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { setExpanded(true) }
                    .padding(Dimen.spacing_m1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedUsers.isEmpty()) stringResource(R.string.select_person) else stringResource(
                        R.string.selected_people,
                        selectedUsers.joinToString { it.firstName.ifEmpty { it.userName } }),
                    fontSize = Dimen.font_size_m1,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier.size(Dimen.spacing_l)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (availableUsers.isEmpty()) {
                    DropdownMenuItem(onClick = { setExpanded(false) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_one_left_to_select))
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
                                fontSize = Dimen.font_size_m1,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = Dimen.spacing_s1)
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
                                    modifier = Modifier.size(Dimen.spacing_m1)
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .heightIn(max = Dimen.spacing_xxxxl2)
                            .verticalScroll(rememberScrollState())
                    ) {
                        availableUsers.sortedBy { it.firstName.ifEmpty { it.userName } }
                            .forEach { person ->
                                DropdownMenuItem(
                                    onClick = {
                                        if (selectedUsers.contains(person)) {
                                            setSelectedPersons(selectedUsers - person)
                                        } else if (selectedUsers.size < maxPlayers) {
                                            setSelectedPersons(selectedUsers + person)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                context.getString(
                                                    R.string.team_max_players,
                                                    teamName,
                                                    maxPlayers
                                                ),
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
                                            checked = selectedUsers.contains(person),
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color.Green
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(Dimen.spacing_xs))
                                        AsyncImage(
                                            model = person.photoUrl,
                                            placeholder = painterResource(id = R.drawable.ic_clock),
                                            error = painterResource(id = R.drawable.ic_clock),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(Dimen.spacing_xl1)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop,
                                            alignment = Alignment.Center
                                        )
                                        Spacer(modifier = Modifier.width(Dimen.spacing_xs))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${person.firstName.ifEmpty { person.userName }} ${person.surname}"
                                            )
                                            Text(
                                                text = person.position,
                                                style = MaterialTheme.typography.body2
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_star),
                                            contentDescription = null,
                                            modifier = Modifier.size(Dimen.spacing_m1)
                                        )
                                        Spacer(modifier = Modifier.width(Dimen.spacing_xxs))
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