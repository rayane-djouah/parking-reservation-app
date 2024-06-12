package com.example.m_parking.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.m_parking.data.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservations(reservations: List<ReservationEntity>)

    @Query("SELECT * FROM reservations")
    fun getReservations(): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE id = :id")
    fun getReservation(id: Int): Flow<ReservationEntity>
}