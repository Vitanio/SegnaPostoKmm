package com.example.segnaposto.android.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.segnaposto.feature.savePark.ParkViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.segnaposto.android.ui.dialog.BaseDialog
import com.example.segnaposto.android.ui.dialog.LocationPermissionTextProvider
import com.example.segnaposto.android.ui.dialog.PermissionDialog
import com.example.segnaposto.data.local.DatabaseDriverFactory
import com.example.segnaposto.dialog.BaseTextProvider
import com.example.segnaposto.feature.savePark.ParkEvent
import com.example.segnaposto.feature.savePark.ParkRepository
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import com.example.segnaposto.util.LocationManager

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ParkScreen(
    navController: NavController,
    activity: Activity,
    applicationContext: Context
) {

    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repository =
                ParkRepository(DatabaseDriverFactory(applicationContext).createDriver())
            val permissionsUtil = LocationManager(context = activity)
            return ParkViewModel(repository = repository, locationManager = permissionsUtil) as T
        }
    }

    val viewModel: ParkViewModel = viewModel(key = null, factory = factory)
    val parkState by viewModel.parkState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionDialog = remember { mutableStateOf(false to false) } // show dialog, show rationale
    val baseDialog = remember { mutableStateOf(false to BaseTextProvider()) } // show dialog, text provider

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { _ ->
            viewModel.onEvent(event = ParkEvent.OnAddParkClicked)
        }
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                }

                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onEvent(event = ParkEvent.OnScreenResumed)
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

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ParkScreenEvent.RequestPermission -> {
                    multiplePermissionResultLauncher.launch(permissionsToRequest)
                }
                is ParkScreenEvent.ShowPermissionDialog -> {
                    permissionDialog.value = event.isVisible to event.isRationale
                }
                is ParkScreenEvent.ShowDialog -> {
                    baseDialog.value = true to event.textProvider
                }
                else -> {}
            }
        }
    }

    // ------- Dialog --------

    if (permissionDialog.value.first) {
        PermissionDialog(
            permissionTextProvider = LocationPermissionTextProvider(),
            isPermanentlyDeclined = permissionDialog.value.second,
            onDismiss = { permissionDialog.value = false to false},
            onOkClick = {
                permissionDialog.value = false to false
                multiplePermissionResultLauncher.launch(permissionsToRequest)
            },
            onGoToAppSettingsClick = {
                viewModel.onEvent(event = ParkEvent.OnGoToSettingsClicked)
                permissionDialog.value = false to false
            }
        )
    }

    if (baseDialog.value.first) {
        BaseDialog(
            textProvider = baseDialog.value.second,
            onDismiss = { baseDialog.value = false to BaseTextProvider() },
            onOkClick = {
                baseDialog.value = false to BaseTextProvider()
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                viewModel.onEvent(event = ParkEvent.OnAddParkClicked)
            }
        ) {
            Text("Add Park Button")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            items(parkState.parkHistory) { element ->
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

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

