package com.yeceylan.groupmaker.ui.onboarding.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class OnBoardingScreens  {

    @Serializable
    data object OnBoardingScreen : OnBoardingScreens()


}