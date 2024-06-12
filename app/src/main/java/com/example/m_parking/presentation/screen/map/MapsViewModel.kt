package com.example.m_parking.presentation.screen.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m_parking.data.model.Parking
import com.example.m_parking.data.repository.ParkingRepository
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: ParkingRepository
) : ViewModel() {
    var state by mutableStateOf(
        MapState(
            properties = MapProperties(
                isMyLocationEnabled = true,
            )

        )
    )
    val parkings = mutableStateOf(listOf<Parking>())

    init {
        viewModelScope.launch {
            try {
                val response = repository.getParkings()
                if (response.isSuccessful) {
                    parkings.value = response.body()!!
                }
                Log.i("Parkings: ${parkings.value}", "Parking: ${parkings.value}")
            } catch (e: Exception) {
                Log.e("ParkingViewModel", "Error", e)
            }
        }
    }

    fun onEvent(event: MapEvents) {
        when (event) {
            is MapEvents.ToggleFalloutMap -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isFalloutMap) {
                            null
                        } else MapStyleOptions(MapStyle.json),
                        isTrafficEnabled = !state.isFalloutMap

                    ),
                    isFalloutMap = !state.isFalloutMap
                )
            }

            is MapEvents.OnMapLongClick -> {
                viewModelScope.launch {
                    /*repository.insertParkingSpot(
                        Parking(

                            event.latLng.latitude,
                            event.latLng.longitude
                        )
                    )
                     */
                }
            }
            /*
            is MapEvents.OnInfoWindowLongClick ->{
                viewModelScope.launch {
                    repository.deleteParkingSpot(event.spot)
                }
            }*/

            else -> {
                // do nothing
            }
        }
    }
}