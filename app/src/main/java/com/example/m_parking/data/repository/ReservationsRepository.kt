package com.example.m_parking.data.repository

import com.example.m_parking.data.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

interface ReservationsRepository {
    suspend fun insertReservation(reservation: ReservationEntity)

    suspend fun insertReservations(reservations: List<ReservationEntity>)
    fun getReservations(): Flow<List<ReservationEntity>>

    fun getReservation(id: Int): Flow<ReservationEntity>
}