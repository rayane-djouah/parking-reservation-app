package com.example.m_parking.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.m_parking.data.dao.ReservationDao
import com.example.m_parking.data.model.ReservationEntity

@Database(
    entities = [ReservationEntity::class],
    version = 1
)
abstract class ReservationsDatabase : RoomDatabase() {
    abstract val dao: ReservationDao
}