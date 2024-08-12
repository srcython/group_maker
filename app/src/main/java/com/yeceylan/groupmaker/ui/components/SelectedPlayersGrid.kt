package com.yeceylan.groupmaker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.model.User

@Composable
fun SelectedPlayersGrid(selectedUsers: List<User>, setSelectedPersons: (List<User>) -> Unit) {
    if (selectedUsers.isNotEmpty()) {
        Text(text = "Seçilenler:", modifier = Modifier.padding(top = 5.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .heightIn(max = 400.dp)
        ) {
            items(selectedUsers) { person ->
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
                            AsyncImage(
                                model = person.photoUrl,
                                placeholder = painterResource(id = R.drawable.ic_clock),
                                error = painterResource(id = R.drawable.ic_clock),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )

                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        setSelectedPersons(selectedUsers - person)
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
                            text = person.firstName.ifEmpty { person.userName },
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