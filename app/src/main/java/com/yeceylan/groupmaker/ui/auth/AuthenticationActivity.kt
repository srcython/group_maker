package com.yeceylan.groupmaker.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yeceylan.groupmaker.MainActivity
import com.yeceylan.groupmaker.core.goToTheActivity
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            Scaffold(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                content = { paddingValues ->
                    Surface(modifier = Modifier.padding(paddingValues)) {
                        AuthenticationNavGraph(navController = navController) {
                            goToTheActivity(activityToGo = MainActivity(), isFinish = true)
                        }
                    }
                },
            )
        }
    }
}