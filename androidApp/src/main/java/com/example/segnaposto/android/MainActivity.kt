package com.example.segnaposto.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.segnaposto.Greeting
import com.example.segnaposto.android.ui.ParkScreen
import com.example.segnaposto.data.local.DatabaseDriverFactory
import com.example.segnaposto.data.park.SqlDelightParkDataSource
import com.example.segnaposto.database.ParkDatabase
import com.example.segnaposto.domain.Park
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database =
            SqlDelightParkDataSource(ParkDatabase(DatabaseDriverFactory(applicationContext).createDriver()))

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "park_screen"
                ) {
                    composable(
                        route = "park_screen"
                    ) {
                        ParkScreen(
                            navController = navController,
                            applicationContext = applicationContext
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
