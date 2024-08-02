package com.yeceylan.groupmaker.ui.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yeceylan.groupmaker.ui.sport_types.SportTypes
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HistoryScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.ProfileScreen
import com.yeceylan.groupmaker.ui.match.MakeMatchScreen
import com.yeceylan.groupmaker.ui.onboarding.OnBoarding
import com.yeceylan.groupmaker.ui.splash.SplashScreen
import com.yeceylan.groupmaker.ui.splash.navigation.SplashScreens
import com.yeceylan.groupmaker.ui.sport_types.SportTypeSetting
import com.yeceylan.groupmaker.ui.sport_types.navigation.SportTypeScreens


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
        composable<AuthenticationScreens.SignUpScreen> {
            isShowBottomBar.value = false
            SignUpScreen(navController = navController)
        }
        composable<AuthenticationScreens.MakeMatchScreen> {
            isShowBottomBar.value = false
            MakeMatchScreen(navController = navController)
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
            SportTypes(navController = navController)
        }
        composable(route = BottomBarScreen.History.route) {
            isShowBottomBar.value = true
            HistoryScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            isShowBottomBar.value = true
            ProfileScreen()
        }
       /* composable<SportTypeScreens.SportTypeSetting> {
            isShowBottomBar.value = true
            SportTypeSetting()

        }*/
        composable(
            route = SportTypeScreens.SportTypeSetting.route,
            arguments = listOf(
                navArgument("title"){ type = NavType.StringType},
                navArgument("size"){ type = NavType.IntType}
            )
        ) {
            val title = it.arguments?.getString("title")!!
            val size = it.arguments?.getInt("size")!!
            isShowBottomBar.value = true
            SportTypeSetting(title,size)

        }
    }
}
