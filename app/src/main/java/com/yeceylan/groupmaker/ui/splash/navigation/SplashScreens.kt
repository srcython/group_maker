package com.yeceylan.groupmaker.ui.splash.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SplashScreens(val route: String) {

    @Serializable
    data object SplashScreen : SplashScreens("splash_screen")

    @Serializable
    data object OnboardingScreen : SplashScreens("onboarding_screen")
}