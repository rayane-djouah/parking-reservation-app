package com.example.m_parking.presentation.screen.map

import com.example.m_parking.data.model.Parking
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val isFalloutMap: Boolean = false,
    val parkingSpots: List<Parking> = emptyList()
)
