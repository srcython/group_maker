package com.yeceylan.groupmaker.ui.bottombar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HistoryScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HomeScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.ProfileScreen
import com.yeceylan.groupmaker.ui.splash.SplashScreen
import com.yeceylan.groupmaker.ui.splash.navigation.SplashScreens
import com.yeceylan.groupmaker.ui.splash.onboarding.OnBoarding


@Composable
fun MainNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreens.SplashScreen.route,
    ) {

        composable(route = AuthenticationScreens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AuthenticationScreens.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = SplashScreens.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = SplashScreens.OnboardingScreen.route) {
            OnBoarding(navController = navController)
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.History.route) {
            HistoryScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}
