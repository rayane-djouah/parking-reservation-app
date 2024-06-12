package com.example.m_parking.data.repository

import com.example.m_parking.data.dao.ReservationDao
import com.example.m_parking.data.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

class ReservationsRepositoryImpl(
    private val dao: ReservationDao
) : ReservationsRepository {
    override suspend fun insertReservation(res: ReservationEntity) {
        dao.insertReservation(res)
    }

    override suspend fun insertReservations(reservations: List<ReservationEntity>) {
        dao.insertReservations(reservations)
    }

    override fun getReservations(): Flow<List<ReservationEntity>> {
        return dao.getReservations()
    }

    override fun getReservation(id: Int): Flow<ReservationEntity> {
        return dao.getReservation(id)
    }
}