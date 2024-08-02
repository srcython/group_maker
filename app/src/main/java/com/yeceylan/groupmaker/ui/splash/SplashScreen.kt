package com.yeceylan.groupmaker.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseAuth
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.bottombar.BottomBarScreen
import com.yeceylan.groupmaker.ui.splash.navigation.SplashScreens
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {

    val alpha = remember {
        Animatable(0f)
    }
    val auth = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            1f,
            animationSpec = tween(2500)
        )
        delay(3000)

        if (auth != null) {
            //clear back stack
            navController.navigate(BottomBarScreen.Home.route){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
        } else {
            navController.navigate(SplashScreens.OnboardingScreen){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoaderAnimation(
            modifier = Modifier.size(400.dp), anim = R.raw.splash_lottie
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Let's Play",
            modifier = Modifier.alpha(alpha.value),
            fontSize = 52.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition, iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}
