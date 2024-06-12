package com.example.m_parking.data.repository

import com.example.m_parking.data.api.ParkingApi
import com.example.m_parking.data.model.ReservationRequestBody
import javax.inject.Inject

class ParkingRepository @Inject constructor(private val parkingApi: ParkingApi) {
    suspend fun getParkings() = parkingApi.getParkings()
    suspend fun getParking(id: Int) = parkingApi.getParking(id)

    suspend fun reserveParking(id: Int, reservation: ReservationRequestBody) =
        parkingApi.bookParking(id, reservation)
}