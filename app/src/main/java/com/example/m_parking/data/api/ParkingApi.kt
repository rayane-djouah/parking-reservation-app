package com.example.m_parking.data.api

import com.example.m_parking.data.model.Parking
import com.example.m_parking.data.model.Reservation
import com.example.m_parking.data.model.ReservationRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ParkingApi {
    @GET("/parkings")
    suspend fun getParkings(): Response<List<Parking>>

    @GET("parkings/{id}")
    suspend fun getParking(@Path("id") id: Int): Response<Parking>

    @POST("parkings/{id}/reservations")
    suspend fun bookParking(
        @Path("id") id: Int,
        @Body reservation: ReservationRequestBody
    ): Response<Reservation>
}