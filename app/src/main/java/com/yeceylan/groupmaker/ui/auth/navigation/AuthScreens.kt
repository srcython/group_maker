package com.yeceylan.groupmaker.ui.auth.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthenticationScreens {

    @Serializable
    data object LoginScreen : AuthenticationScreens()

    @Serializable
    data object SignUpScreen : AuthenticationScreens()
}
