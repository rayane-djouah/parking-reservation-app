package com.example.m_parking.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.m_parking.data.repository.AuthTokenStoreRepository
import com.example.m_parking.data.repository.ReservationsRepository
import com.example.m_parking.data.repository.ReservationsRepositoryImpl
import com.example.m_parking.data.database.ReservationsDatabase
import com.example.m_parking.data.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParkingModule {
    @Singleton
    @Provides
    fun providesReservationsDatabase(app: Application): ReservationsDatabase {
        return Room.databaseBuilder(
            app,
            ReservationsDatabase::class.java,
            "reservations_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context) =
        DataStoreRepository(context = context)

    @Provides
    @Singleton
    fun provideAuthTokenStoreRepository(@ApplicationContext context: Context) =
        AuthTokenStoreRepository(context = context)

    @Singleton
    @Provides
    fun provideReservationsDatabase(db: ReservationsDatabase): ReservationsRepository {
        return ReservationsRepositoryImpl(db.dao)
    }

}