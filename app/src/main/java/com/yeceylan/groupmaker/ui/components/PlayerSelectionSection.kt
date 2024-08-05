package com.yeceylan.groupmaker.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.match.Person

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
