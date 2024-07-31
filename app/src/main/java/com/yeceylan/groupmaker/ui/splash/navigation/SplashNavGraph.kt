package com.yeceylan.groupmaker.ui.splash.navigation


import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeceylan.groupmaker.ui.splash.SplashScreen
import com.yeceylan.groupmaker.ui.onboarding.OnBoarding

@Composable
fun SplashNavGraph(navController: NavHostController, goToTheActivity: (activity: Activity) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = SplashScreens.SplashScreen,
    ) {
        composable<SplashScreens.SplashScreen> {
            SplashScreen(navController = navController)
        }

        composable<SplashScreens.OnboardingScreen> {
            OnBoarding(navController = navController)

        }
    }
}