package com.yeceylan.groupmaker.ui.oldMatches

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.model.user.User

@Composable
fun OldMatchesScreen(
    viewModel: OldMatchesViewModel = hiltViewModel()
) {
    val oldMatchesResource by viewModel.oldMatches.collectAsState()
    var expandedMatchId by remember { mutableStateOf<String?>(null) }
    var showResultDialog by remember { mutableStateOf(false) }
    var currentMatch by remember { mutableStateOf<Match?>(null) }

    when (oldMatchesResource) {
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp),
                color = MaterialTheme.colors.primary
            )
        }

        is Resource.Success -> {
            val matches = oldMatchesResource.data ?: emptyList()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(matches) { match ->
                    MatchItem(
                        match = match,
                        isExpanded = expandedMatchId == match.id,
                        viewModel = viewModel,
                        onExpandClick = { selectedMatchId ->
                            expandedMatchId = if (expandedMatchId == selectedMatchId) null else selectedMatchId
                        },
                        onAddResultClick = {
                            currentMatch = match
                            showResultDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        is Resource.Error -> {
            Text(
                text = oldMatchesResource.message ?: stringResource(R.string.an_error_occured),
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    if (showResultDialog && currentMatch != null) {
        AddResultDialog(onDismiss = { showResultDialog = false }) { result ->
            viewModel.updateMatchResult(currentMatch!!, result)
            showResultDialog = false
        }
    }
}

@Composable
fun AddResultDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var result by remember { mutableStateOf("") }

    AlertDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_result)) },
        text = {
            OutlinedTextField(
                value = result,
                onValueChange = { result = it },
                label = { Text(stringResource(R.string.enter_result)) }
            )
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(result) },
                colors = ButtonDefaults.buttonColors(Color.Blue),
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(Color.Blue),
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun MatchItem(
    match: Match,
    isExpanded: Boolean,
    viewModel: OldMatchesViewModel,
    onExpandClick: (String) -> Unit,
    onAddResultClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(16.dp)),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        text = "${match.firstTeamName} vs ${match.secondTeamName}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date")
                        Text(text = match.matchDate ?: "")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = match.matchTime ?: "")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location")
                        Text(text = match.matchLocationTitle ?: "")
                    }
                }

                if (match.result == null) {
                    Button(
                        onClick = onAddResultClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = "Sonuç Ekle",
                            color = Color.White,
                        )
                    }
                } else {
                    Button(
                        onClick = { /* sonar - comment */ },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = match.result,
                            color = Color.White,
                        )
                    }
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = match.firstTeamName ?: "", fontWeight = FontWeight.Bold)
                match.firstTeamPlayerList.forEach { player ->
                    PlayerRatingItem(player = player, matchId = match.id, viewModel = viewModel)
                }

                Text(text = match.secondTeamName ?: "", fontWeight = FontWeight.Bold)
                match.secondTeamPlayerList.forEach { player ->
                    PlayerRatingItem(player = player, matchId = match.id, viewModel = viewModel)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = { onExpandClick(match.id) },
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(50.dp))
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Toggle details",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun PlayerRatingItem(player: User, matchId: String, viewModel: OldMatchesViewModel) {
    var rating by remember { mutableIntStateOf(0) }
    var isRatingSubmitted by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(2.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(16.dp)),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.player, player.userName))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    (1..5).forEach { starIndex ->
                        IconButton(onClick = {
                            if (!isRatingSubmitted) rating = starIndex
                        }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rate",
                                tint = if (starIndex <= rating) Color.Yellow else Color.Gray
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.updatePlayerRating(matchId, player.id, rating)
                        isRatingSubmitted = true
                    },
                    enabled = !isRatingSubmitted,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Submit Rating",
                        tint = Color.White
                    )
                }
            }
        }
    }
}