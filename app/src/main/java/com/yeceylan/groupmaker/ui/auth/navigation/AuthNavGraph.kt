package com.yeceylan.groupmaker.ui.auth.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen

@Composable
fun AuthenticationNavGraph(navController: NavHostController, goToTheActivity: (activity: Activity) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = AuthenticationScreens.LoginScreen,
    ) {
        composable<AuthenticationScreens.LoginScreen> {
            LoginScreen(navController = navController) {
                goToTheActivity(it)
            }
        }

        composable<AuthenticationScreens.SignUpScreen> {
            SignUpScreen(navController = navController) {
                goToTheActivity(it)
            }
        }
    }
}