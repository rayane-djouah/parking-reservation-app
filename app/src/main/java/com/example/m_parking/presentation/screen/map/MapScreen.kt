package com.example.m_parking.presentation.screen.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.m_parking.navigation.Screens
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Log.i("MapScreen", "Parkings: ${viewModel.parkings.value}")
    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false
        )
    }
    val alger = LatLng(36.752887, 3.042048)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(alger, 12f)
    }
    Scaffold(
        modifier = Modifier.fillMaxHeight(),
    ) {
        GoogleMap(
            modifier = Modifier,
            cameraPositionState = cameraPositionState,
            properties = viewModel.state.properties,
            uiSettings = uiSettings,
            onMapLongClick = {
                viewModel.onEvent(MapEvents.OnMapLongClick(it))
            },
        ) {
            viewModel.parkings.value.forEach { parking ->
                Marker(
                    state = MarkerState(
                        position = LatLng(parking.latitude, parking.longitude)
                    ),
                    title = "Parking: ${parking.name}",
                    snippet = "Long click to view details",
                    onInfoWindowLongClick = {
                        val parkingid = parking.id
                        navController.navigate("${Screens.ParkingScreen.route}/$parkingid")
                    },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            }
        }
    }
}