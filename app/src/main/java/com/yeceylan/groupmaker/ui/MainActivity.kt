package com.yeceylan.groupmaker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.yeceylan.groupmaker.ui.bottombar.BottomBar
import com.yeceylan.groupmaker.ui.theme.GroupMakerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isBottomBarShow = remember { mutableStateOf(true) }


            GroupMakerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { if (isBottomBarShow.value) {
                        BottomBar(navController = navController)
                    } }
                ) {
                    MainNavGraph(navController = navController, isShowBottomBar =  isBottomBarShow)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GroupMakerTheme {
        Greeting("Android")
    }
}