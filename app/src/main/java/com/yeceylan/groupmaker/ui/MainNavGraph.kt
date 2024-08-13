package com.yeceylan.groupmaker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.google.gson.Gson
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.ui.auth.login.LoginScreen
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.auth.signup.SignUpScreen
import com.yeceylan.groupmaker.ui.bottombar.BottomBarScreen
import com.yeceylan.groupmaker.ui.match.MakeMatchScreen
import com.yeceylan.groupmaker.ui.match.matchinfo.MatchInfoScreen
import com.yeceylan.groupmaker.ui.match.navigation.MatchScreens
import com.yeceylan.groupmaker.ui.oldMatches.OldMatchesScreen
import com.yeceylan.groupmaker.ui.onboarding.OnBoarding
import com.yeceylan.groupmaker.ui.onboarding.navigation.OnBoardingScreens
import com.yeceylan.groupmaker.ui.player.PlayerPage
import com.yeceylan.groupmaker.ui.player.navigation.PlayerScreens
import com.yeceylan.groupmaker.ui.profile.ProfileScreen
import com.yeceylan.groupmaker.ui.splash.SplashScreen
import com.yeceylan.groupmaker.ui.splash.navigation.SplashScreens
import com.yeceylan.groupmaker.ui.sport_types.SportTypeSetting
import com.yeceylan.groupmaker.ui.sport_types.SportTypes
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
        composable<MatchScreens.MakeMatchScreen> {
            val args = it.toRoute<MatchScreens.MakeMatchScreen>()
            isShowBottomBar.value = false
            MakeMatchScreen(args.size, navController = navController)
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
            Box(modifier = Modifier.padding(bottom = 50.dp)) {
                SportTypes(navController = navController)
            }
        }
        composable(route = BottomBarScreen.History.route) {
            isShowBottomBar.value = true
            Box(modifier = Modifier.padding(bottom = 50.dp, top = 10.dp)) {
                OldMatchesScreen()
            }
        }
        composable(route = BottomBarScreen.Profile.route) {
            isShowBottomBar.value = true
            Box(modifier = Modifier.padding(bottom = 50.dp)) {
                ProfileScreen(navController = navController)
            }
        }
        composable<SportTypeScreens.SportTypeSetting> {
            isShowBottomBar.value = true
            val args = it.toRoute<SportTypeScreens.SportTypeSetting>()
            Box(modifier = Modifier.padding(bottom = 50.dp)) {
                SportTypeSetting(args.title, args.teamSize, navController)
            }
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
            Box(modifier = Modifier.padding(bottom = 50.dp)) {
                PlayerPage(navController = navController)
            }
        }
    }
}