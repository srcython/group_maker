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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.match.Person


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
