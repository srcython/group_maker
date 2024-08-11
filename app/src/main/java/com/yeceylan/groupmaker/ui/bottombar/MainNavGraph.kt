package com.yeceylan.groupmaker.ui.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.MatchInfo
import com.yeceylan.groupmaker.ui.sport_types.SportTypes
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen
import com.yeceylan.groupmaker.ui.bottombar.demoscreens.HistoryScreen
import com.yeceylan.groupmaker.ui.profile.ProfileScreen
import com.yeceylan.groupmaker.ui.match.MakeMatchScreen
import com.yeceylan.groupmaker.ui.match.matchinfo.MatchInfoScreen
import com.yeceylan.groupmaker.ui.match.navigation.MatchScreens
import com.yeceylan.groupmaker.ui.onboarding.OnBoarding
import com.yeceylan.groupmaker.ui.onboarding.navigation.OnBoardingScreens
import com.yeceylan.groupmaker.ui.player.PlayerPage
import com.yeceylan.groupmaker.ui.player.navigation.PlayerScreens
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

        composable<SplashScreens.SplashScreen> {
            isShowBottomBar.value = false
            SplashScreen(navController = navController)
        }
        composable<OnBoardingScreens.OnBoardingScreen> {
            isShowBottomBar.value = false
            OnBoarding(navController = navController)
        }
        composable<AuthenticationScreens.LoginScreen> {
            isShowBottomBar.value = false
            LoginScreen(navController = navController)
        }
        composable<AuthenticationScreens.SignUpScreen> {
            isShowBottomBar.value = false
            SignUpScreen(navController = navController)
        }
        composable(
            route = MatchScreens.MakeMatchScreen().route,
            arguments = listOf(navArgument("teamSize"){ type = NavType.IntType}),
        ) {
            val teamSize = it.arguments?.getInt("teamSize")!!
            isShowBottomBar.value = false
            MakeMatchScreen(teamSize,navController = navController)
        }
        composable<SplashScreens.SplashScreen> {
            isShowBottomBar.value = false
            SplashScreen(navController = navController)
        }
        composable<OnBoardingScreens.OnBoardingScreen> {
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
            SportTypeSetting(title,size,navController)
        }
        composable(
            route = "matchInfo/{matchJson}",
            arguments = listOf(navArgument("matchJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val matchJson = backStackEntry.arguments?.getString("matchJson") ?: return@composable
            val match = Gson().fromJson(matchJson, Match::class.java)
            MatchInfoScreen(navController = navController, match = match)
        }
        composable<PlayerScreens.PlayerPage> {
            isShowBottomBar.value = true
            PlayerPage()
        }
    }
}
