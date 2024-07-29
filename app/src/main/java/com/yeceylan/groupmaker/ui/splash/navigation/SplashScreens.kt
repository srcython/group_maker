package com.yeceylan.groupmaker.ui.splash.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SplashScreens {

    @Serializable
    data object SplashScreen : SplashScreens()

    @Serializable
    data object OnboardingScreen : SplashScreens()
}