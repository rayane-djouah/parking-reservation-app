package com.example.m_parking.data.dto

data class LoginResponseDto(
    val token: String
)

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class RegisterResponseDto(
    val token: String
)