package com.example.m_parking.presentation.screen.map

import com.example.m_parking.data.model.Parking
import com.google.android.gms.maps.model.LatLng

sealed class MapEvents {
    object ToggleFalloutMap : MapEvents()
    data class OnMapLongClick(val latLng: LatLng) : MapEvents()
    data class OnInfoWindowLongClick(val spot: Parking) : MapEvents()
    object OnMyLocationClick : MapEvents()
}
