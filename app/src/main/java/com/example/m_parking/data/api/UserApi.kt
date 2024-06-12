package com.example.m_parking.data.api

import com.example.m_parking.data.dto.LoginRequestDto
import com.example.m_parking.data.dto.LoginResponseDto
import com.example.m_parking.data.dto.RegisterResponseDto
import com.example.m_parking.data.model.User
import com.example.m_parking.data.model.FirebaseToken
import com.example.m_parking.data.model.Reservation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("users/register")
    suspend fun registerUser(@Body user: User): Response<RegisterResponseDto>

    @POST("users/login")
    suspend fun loginUser(@Body loginReq: LoginRequestDto): Response<LoginResponseDto>

    @GET("users/profile")
    suspend fun getUser(): Response<User>

    @GET("users/reservations")
    suspend fun userReservations(): Response<List<Reservation>>

    @GET("users/reservations/{reservationId}")
    suspend fun userReservationById(@Path("reservationId") reservationId: Int): Response<Reservation>

    @POST("users/firebase-token")
    suspend fun sendFirebaseToken(@Body token: FirebaseToken): Response<Void>
}
