package com.yeceylan.groupmaker.ui.bottombar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeceylan.groupmaker.SportTypes
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.login.LoginViewModel
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen
import com.yeceylan.groupmaker.ui.auth.signup.SignUpViewModel
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HistoryScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HomeScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.ProfileScreen
import com.yeceylan.groupmaker.ui.onboarding.OnBoarding
import com.yeceylan.groupmaker.ui.splash.SplashScreen
//import kotlinx.serialization.Serializable


@Composable
fun MainNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    signupViewModel: SignUpViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Routes.Splash.route) {
            SplashScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable(route = Routes.Login.route) {
            LoginScreen(navController = navController, loginViewModel = loginViewModel)
        }

        composable(route = Routes.SignUp.route) {
            SignUpScreen(navController = navController, viewModel = signupViewModel)
        }
        composable(route = Routes.Main.route) {
            BottomNav()
        }
        composable(Routes.OnBoarding.route) {
            OnBoarding(navController = navController)
        }
    }
}

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
    data object Main : Routes("main")
    data object OnBoarding : Routes("onboarding")
}


