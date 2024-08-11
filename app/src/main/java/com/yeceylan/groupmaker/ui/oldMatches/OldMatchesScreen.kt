package com.yeceylan.groupmaker.ui.oldMatches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.User

@Composable
fun OldMatchesScreen(
    viewModel: OldMatchesViewModel = hiltViewModel()
) {
    val oldMatchesResource by viewModel.oldMatches.collectAsState()
    var expandedMatchId by remember { mutableStateOf<String?>(null) } // Track expanded match

    when (oldMatchesResource) {
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = MaterialTheme.colors.primary
            )
        }
        is Resource.Success -> {
            val matches = oldMatchesResource.data ?: emptyList()
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                items(matches) { match ->
                    MatchItem(match = match, isExpanded = expandedMatchId == match.id, viewModel = viewModel) { selectedMatchId ->
                        expandedMatchId = if (expandedMatchId == selectedMatchId) null else selectedMatchId
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is Resource.Error -> {
            Text(
                text = oldMatchesResource.message ?: "Bir hata oluştu.",
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
@Composable
fun MatchItem(
    match: Match,
    isExpanded: Boolean,
    viewModel: OldMatchesViewModel,
    onExpandClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${match.firstTeamName} vs ${match.secondTeamName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Tarih: ${match.matchDate}")
            Text(text = "Konum: ${match.matchLocationTitle}")

            if (isExpanded) {
                // Expanded content
                Spacer(modifier = Modifier.height(8.dp))
                match.playerList.forEach { player ->
                    PlayerRatingItem(player = player, matchId = match.id, viewModel = viewModel)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onExpandClick(match.id) }) {
                Text(text = if (isExpanded) "Collapse" else "Expand")
            }
        }
    }
}

@Composable
fun PlayerRatingItem(player: User, matchId: String, viewModel: OldMatchesViewModel) {
    var rating by remember { mutableStateOf(0) }
    var isRatingSubmitted by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Player: ${player.userName}")

        Row {
            // Stars for rating
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
            enabled = !isRatingSubmitted
        ) {
            Text("Rate Player")
        }
    }
}

@Composable
fun MatchItem(match: Match) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${match.firstTeamName} vs ${match.secondTeamName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Tarih: ${match.matchDate}")
            Text(text = "Konum: ${match.matchLocationTitle}")
        }
    }
}
