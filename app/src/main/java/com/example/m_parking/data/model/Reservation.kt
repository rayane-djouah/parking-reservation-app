package com.example.m_parking.data.model

data class Reservation(
    val id: Int,
    val userId: Int,
    val parkingId: Int,
    val date: String,
    val entryTime: String,
    val exitTime: String
)

data class ReservationRequestBody(
    val userId: Int,
    val date: String,
    val entryTime: String,
    val exitTime: String
)

fun ReservationEntity.toReservation(): Reservation {
    return Reservation(
        id, userId, parkingId, date, entryTime, exitTime
    )
}

fun Reservation.toReservationEntity(): ReservationEntity {
    return ReservationEntity(
        id, userId, parkingId, date, entryTime, exitTime
    )
}