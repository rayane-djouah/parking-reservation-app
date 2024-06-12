package com.example.m_parking.data.repository

import com.example.m_parking.data.api.UserApi
import com.example.m_parking.data.dto.LoginRequestDto
import com.example.m_parking.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    //login
    suspend fun register(user: User) = userApi.registerUser(user = user)
    suspend fun login(loginReq: LoginRequestDto) = userApi.loginUser(loginReq = loginReq)

    suspend fun getReservations() = userApi.userReservations()

    suspend fun getReservationById(reservationId: Int) =
        userApi.userReservationById(reservationId = reservationId)

    suspend fun getUser() = userApi.getUser()

}