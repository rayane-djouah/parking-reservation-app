package com.example.m_parking.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val parkingId: Int,
    val date: String,
    val entryTime: String,
    val exitTime: String
)