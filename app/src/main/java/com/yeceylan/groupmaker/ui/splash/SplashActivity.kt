package com.yeceylan.groupmaker.ui.splash

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yeceylan.groupmaker.MainActivity
import com.yeceylan.groupmaker.core.goToTheActivity
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationNavGraph
import com.yeceylan.groupmaker.ui.splash.navigation.SplashNavGraph
import com.yeceylan.groupmaker.ui.splash.onboarding.OnBoarding
import com.yeceylan.groupmaker.ui.splash.ui.theme.GroupMakerTheme


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            GroupMakerTheme {
                Scaffold(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    content = { paddingValues ->
                        Surface(modifier = Modifier.padding(paddingValues)) {
                            SplashNavGraph(navController = navController)

                        }
                    },
                )
            }
        }
    }
}



