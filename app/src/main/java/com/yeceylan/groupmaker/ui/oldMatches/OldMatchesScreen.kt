package com.yeceylan.groupmaker.ui.oldMatches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.core.Resource

@Composable
fun OldMatchesScreen(
    viewModel: OldMatchesViewModel = hiltViewModel()
) {
    val oldMatchesResource by viewModel.oldMatches.collectAsState()

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
                    MatchItem(match = match)
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
