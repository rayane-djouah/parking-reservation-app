package com.example.m_parking.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m_parking.data.model.Parking
import com.example.m_parking.data.repository.ParkingRepository
import com.example.m_parking.data.model.ReservationRequestBody
import com.example.m_parking.presentation.viewmodel.ReservationCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository
) : ViewModel() {
    val parkings = mutableStateOf(listOf<Parking>())
    val parking = mutableStateOf(
        Parking(
            commune = "No Commune",
            description = "No Description",
            id = 1,
            latitude = 0.0,
            longitude = 0.0,
            name = "No Parking Name",
            numSlots = 0,
            photo = "No photo_url",
            slotPrice = 0.0
        )
    )
    var reservationCallback: ReservationCallback? = null

    init {
        getParkings()
    }

    fun setCallback(callback: ReservationCallback) {
        reservationCallback = callback
    }

    private fun getParkings() {
        viewModelScope.launch {
            try {
                val response = parkingRepository.getParkings()
                if (response.isSuccessful) {
                    parkings.value = response.body()!!
                }
            } catch (e: Exception) {
                Log.e("ParkingViewModel", "Error", e)
            }
        }
    }

    suspend fun getParkingById(id: Int): Parking {
        viewModelScope.launch {
            try {
                val response = parkingRepository.getParking(id)
                if (response.isSuccessful) {
                    parking.value = response.body()!!
                }
            } catch (e: Exception) {
                Log.e("ParkingViewModel", "Error", e)
            }
        }.join()
        return parking.value
    }

    fun reserveParking(id: Int, reservation: ReservationRequestBody) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = parkingRepository.reserveParking(id, reservation)
                    if (response.isSuccessful) {
                        reservationCallback?.onReservationSuccess()
                        Log.d("reservation", "success")
                    } else (
                            reservationCallback?.onReservationFailure()
                            )
                } catch (e: Exception) {
                    Log.e("reservation", "error", e)
                } finally {

                }
            }
        }
    }
}