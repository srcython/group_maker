package com.yeceylan.groupmaker.ui.auth.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthenticationScreens(val route: String) {

    @Serializable
    data object LoginScreen : AuthenticationScreens("login_screen")

    @Serializable
    data object SignUpScreen : AuthenticationScreens("signup_screen")
}
