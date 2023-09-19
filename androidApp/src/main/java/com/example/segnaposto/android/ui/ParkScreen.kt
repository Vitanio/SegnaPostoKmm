package com.example.segnaposto.android.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.segnaposto.feature.savePark.ParkViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.segnaposto.data.local.DatabaseDriverFactory
import com.example.segnaposto.feature.savePark.ParkRepository

@Composable
fun ParkScreen(
    navController: NavController,
    applicationContext: Context
) {

    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repository = ParkRepository(DatabaseDriverFactory(applicationContext).createDriver())
            return ParkViewModel(repository = repository) as T
        }
    }

    val viewModel: ParkViewModel = viewModel(key = null, factory = factory)
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { viewModel.onParkClicked() }
        ) {
            Text("CLick me")
        }

        Text("Counter value: ${state.test}")
    }
}