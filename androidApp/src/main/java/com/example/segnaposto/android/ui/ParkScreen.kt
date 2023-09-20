package com.example.segnaposto.android.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.segnaposto.feature.savePark.ParkViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.segnaposto.data.local.DatabaseDriverFactory
import com.example.segnaposto.feature.savePark.ParkEvent
import com.example.segnaposto.feature.savePark.ParkRepository

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
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
    val uiEvent by viewModel.uiEvent.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                }
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onEvent(event = ParkEvent.onScreenResumed)
                }
                Lifecycle.Event.ON_STOP -> {
                }
                Lifecycle.Event.ON_DESTROY -> {
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = { viewModel.onEvent(event = ParkEvent.OnAddParkClicked) }
        ) {
            Text("Add Park Button")
        }

        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
            items(uiEvent.parkHistory){ element ->
                Card(modifier = Modifier.padding(10.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Id: ${element.id}")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Title: ${element.title}")
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Latitude: ${element.latitude}")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Longitude: ${element.longitude}")
                        }
                    }
                }
            }
        }
    }
}