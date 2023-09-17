package com.example.segnaposto.android.ui

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
import androidx.navigation.NavController
import com.example.segnaposto.domain.viewModel.ParkViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParkScreen(
    navController: NavController,
    viewModel: ParkViewModel = viewModel()
) {
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