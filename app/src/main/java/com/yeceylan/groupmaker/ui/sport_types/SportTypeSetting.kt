package com.yeceylan.groupmaker.ui.sport_types

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeceylan.groupmaker.R

@Composable
fun SportTypeSetting(title: String, teamSize: Int) {
    val create = painterResource(id = R.drawable.create_match)
    val players = painterResource(id = R.drawable.players)

    Text(
        text = title,
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 70.dp)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        SportTypeSettingItem(painter = create, "Create a Match")
        SportTypeSettingItem(painter = players, "Players")


    }

}

@Composable
fun SportTypeSettingItem(painter: Painter, text: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                Log.e("card", "click")
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),

        ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            // startY = 00f,
                        )
                    )
            ) {

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(text, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }
        }
    }
}