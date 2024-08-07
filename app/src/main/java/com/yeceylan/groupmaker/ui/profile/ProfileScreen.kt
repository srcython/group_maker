package com.yeceylan.groupmaker.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yeceylan.groupmaker.R


@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileScreen( viewModel: ProfileViewModel = hiltViewModel()) {

    val user = viewModel.user.collectAsState().value

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color(android.graphics.Color.parseColor("#ececec"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.height(280.dp)) {
            Image(
                painter = painterResource(id = R.drawable.top_background),
                contentDescription = "",
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom

            ) {
                GlideImage(
                    model = user.photoUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                )
            }
        }

        Text(
            text = user.userName,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
            color = Color(android.graphics.Color.parseColor("#747679"))
        )
        Text(
            text = user.email,
            fontSize = 20.sp,
            color = Color(android.graphics.Color.parseColor("#747679"))
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 32.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Bilgiler",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Adı: ${user.firstName}", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
                Text(text = "Soyadı: ${user.surname}", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
                Text(text = "Pozisyon: ${user.position}", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
                Text(text = "Puan: ${user.point}", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
            }
        }

        Button(
            onClick = { /*TODO*/ },
            Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 10.dp, bottom = 10.dp)
                .height(55.dp), colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(android.graphics.Color.parseColor("#ffffff"))
            ), shape = RoundedCornerShape(15)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .clickable {})
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Profile Settings",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }

    }
}