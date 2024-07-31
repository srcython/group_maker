package com.yeceylan.groupmaker.ui.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HistoryScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HomeScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.ProfileScreen
import com.yeceylan.groupmaker.ui.onboarding.OnBoarding
import com.yeceylan.groupmaker.ui.splash.SplashScreen
import com.yeceylan.groupmaker.ui.splash.navigation.SplashScreens


@Composable
fun MainNavGraph(
    navController: NavHostController,
    isShowBottomBar: MutableState<Boolean>,
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreens.SplashScreen,
    ) {

        composable<AuthenticationScreens.LoginScreen> {
            isShowBottomBar.value = false
            LoginScreen(navController = navController)
        }
        composable< AuthenticationScreens.SignUpScreen> {
            isShowBottomBar.value = false
            SignUpScreen(navController = navController)
        }
        composable<SplashScreens.SplashScreen> {
            isShowBottomBar.value = false
            SplashScreen(navController = navController)
        }
        composable<SplashScreens.OnboardingScreen> {
            isShowBottomBar.value = false
            OnBoarding(navController = navController)
        }
        composable(route = BottomBarScreen.Home.route) {
            isShowBottomBar.value = true
            HomeScreen()
        }
        composable(route = BottomBarScreen.History.route) {
            isShowBottomBar.value = true
            HistoryScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            isShowBottomBar.value = true
            ProfileScreen()
        }
    }
}
