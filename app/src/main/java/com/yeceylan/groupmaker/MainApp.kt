package com.yeceylan.groupmaker

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.yeceylan.groupmaker.ui.auth.login.LoginViewModel
import com.yeceylan.groupmaker.ui.bottombar.MainNavGraph

@Composable
fun MainApp(loginViewModel: LoginViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    MainNavGraph(navController = navController, loginViewModel = loginViewModel)
}
