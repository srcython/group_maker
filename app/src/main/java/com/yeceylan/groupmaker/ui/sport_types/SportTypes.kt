package com.yeceylan.groupmaker.ui.sport_types

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.sport_types.navigation.SportTypeScreens
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_m1
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_xl
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_c
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_cc
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_l
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_m2
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_s1
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_s2
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_xxl

@Composable
fun SportTypes(navController: NavController, viewModel: SportTypeViewModel = hiltViewModel()) {

    val list = viewModel.sportTypeList.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .size(spacing_c))
        Text(
            text = stringResource(id = R.string.select_sport),
            fontSize = font_size_xl,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .size(spacing_xxl))
        LazyColumn {

            items(list) {
                ImageCard(
                    painter = it.image!!,
                    contentDescription = stringResource(id = R.string.card_image_desc),
                    title = it.title!!,
                    navController = navController,
                    teamSize = it.size!!
                )
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
    navController: NavController,
    teamSize: Int

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing_m2)
            .clickable {
                navController.navigate(SportTypeScreens.SportTypeSetting(title, teamSize))
            },
        shape = RoundedCornerShape(spacing_l),
        elevation = CardDefaults.cardElevation(spacing_s1),

        ) {
        Box(modifier = Modifier.height(spacing_cc)) {

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
                        ),
                    ),
            ) {

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing_s2),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = font_size_m1,
                        fontStyle = FontStyle.Italic,
                    )
                )
            }
        }
    }
}