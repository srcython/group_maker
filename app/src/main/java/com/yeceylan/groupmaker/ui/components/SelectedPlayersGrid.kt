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
import coil.compose.AsyncImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.ui.theme.Dimen

@Composable
fun SelectedPlayersGrid(selectedUsers: List<User>, setSelectedPersons: (List<User>) -> Unit) {
    if (selectedUsers.isNotEmpty()) {
        Text(text = "Seçilenler:", modifier = Modifier.padding(top = Dimen.spacing_xxs))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(Dimen.spacing_xs),
            modifier = Modifier
                .heightIn(max = Dimen.spacing_xxxxxl)
        ) {
            items(selectedUsers) { person ->
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .padding(Dimen.spacing_xs)
                        .size(Dimen.spacing_xxxl1)
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
                                    .size(Dimen.spacing_xxl2)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )

                            Box(
                                modifier = Modifier
                                    .size(Dimen.spacing_m1)
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        setSelectedPersons(selectedUsers - person)
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_remove),
                                    contentDescription = "Remove",
                                    modifier = Modifier.size(Dimen.spacing_m1)
                                )
                            }
                        }
                        Text(
                            text = person.firstName.ifEmpty { person.userName },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = Dimen.spacing_xxs)
                        )
                    }
                }
            }
        }
    }
}