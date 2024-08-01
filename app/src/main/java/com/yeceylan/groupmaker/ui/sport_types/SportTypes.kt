package com.yeceylan.groupmaker.ui.sport_types

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yeceylan.groupmaker.core.Response
import com.yeceylan.groupmaker.ui.sport_types.navigation.SportTypeScreens

@Composable
fun SportTypes(navController: NavController, viewModel: SportTypeViewModel = hiltViewModel()) {

    when (val sportListResponse = viewModel.booksResponse) {
        is Response.Failure -> "TODO()"
        is Response.Loading -> "TODO()"
        is Response.Success ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn() {
                    val a = sportListResponse.data

                    items(sportListResponse.data) {

                        ImageCard(
                            painter = it.image!!,
                            contentDescription = "",
                            title = it.title!!,
                            navController = navController,
                        )
                    }
                }
            }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCard(
    painter: String,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                navController.navigate(SportTypeScreens.SportTypeSetting)
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),

        ) {
        Box(modifier = Modifier.height(200.dp)) {

            GlideImage(
                model = painter,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
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
                            // startY = 100f,
                        ),
                    ),
            ) {}
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                    )
                )
            }
        }
    }
}