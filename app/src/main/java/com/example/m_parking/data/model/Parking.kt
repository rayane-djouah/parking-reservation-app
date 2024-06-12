package com.example.m_parking.data.model

data class Parking(
    val id: Int,
    val photo: String,
    val name: String,
    val commune: String,
    val description: String?,
    val slotPrice: Double,
    val numSlots: Int,
    val latitude: Double,
    val longitude: Double
)