package com.yeceylan.groupmaker.ui.sport_types

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.components.button.BackButton
import com.yeceylan.groupmaker.ui.match.navigation.MatchScreens
import com.yeceylan.groupmaker.ui.player.navigation.PlayerScreens

@Composable
fun SportTypeSetting(
    title: String,
    teamSize: Int,
    navController: NavController,
) {
    val create = painterResource(id = R.drawable.create_match)
    val players = painterResource(id = R.drawable.players)

    Row(modifier = Modifier.padding(top = 60.dp, start = 20.dp)) {
        BackButton {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Text(
            text = title,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
        )
        LazyColumn {
            item {
                SportTypeSettingItem(
                    painter = create,
                    stringResource(id = R.string.card_title_create),
                    navController,
                    teamSize,title,
                )
            }
            item {
                SportTypeSettingItem(
                    painter = players,
                    stringResource(id = R.string.card_title_players),
                    navController,
                    teamSize,title,
                )
            }
        }
    }
}

@Composable
fun SportTypeSettingItem(
    painter: Painter,
    text: String,
    navController: NavController,
    teamSize: Int,
    title: String,
    viewModel:SportTypeViewModel = hiltViewModel(),
    ) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                viewModel.addMatch(title, teamSize)
                if (text != "Oyuncular") {
                    navController.navigate(MatchScreens.MakeMatchScreen(teamSize))
                } else {
                    navController.navigate(PlayerScreens.PlayerPage)
                }
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),

        ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black,
                            ),
                        )
                    )
            ) {

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(text, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }
        }
    }
}