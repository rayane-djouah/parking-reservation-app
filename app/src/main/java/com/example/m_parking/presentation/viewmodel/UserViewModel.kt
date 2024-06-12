package com.example.m_parking.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m_parking.core.services.FirebaseNotificationsService
import com.example.m_parking.data.dto.LoginRequestDto
import com.example.m_parking.data.model.User
import com.example.m_parking.data.repository.UserRepository
import com.example.m_parking.data.model.Reservation
import com.example.m_parking.data.model.toReservation
import com.example.m_parking.data.model.toReservationEntity
import com.example.m_parking.data.repository.AuthTokenStoreRepository
import com.example.m_parking.data.repository.FirebaseTokenStoreRepository
import com.example.m_parking.data.repository.ReservationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reservationsRepository: ReservationsRepository,
    private val tokenRepository: AuthTokenStoreRepository
) : ViewModel() {
    val reservations = mutableStateOf(listOf<Reservation>())
    val reservation = mutableStateOf(
        Reservation(
            id = 1,
            parkingId = 1,
            userId = 1,
            date = "../../..",
            entryTime = "00:00",
            exitTime = "00:00",
        )
    )
    private val _userLoginResult = MutableStateFlow(false)
    val userLoginResult: StateFlow<Boolean> = _userLoginResult.asStateFlow()

    val user = mutableStateOf(
        User(
            "example@esi.dz", "admin", "User First Name", "User Last Name", ""
        )
    )

    init {
        getUser()
        getReservations()
    }

    fun getReservations() {
        viewModelScope.launch {
            try {
                val response = userRepository.getReservations()
                if (response.isSuccessful) {
                    reservations.value = response.body()!!
                    // convert to entity
                    val reservationsEntity = reservations.value.map { it.toReservationEntity() }
                    // insert in db
                    response.body()
                        ?.let { reservationsRepository.insertReservations(reservationsEntity) }
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error", e)
                reservationsRepository.getReservations().let {
                    it.collect {
                        reservations.value = it.map {
                            it.toReservation()
                        }
                    }
                }
            }
        }
    }

    suspend fun getReservationById(id: Int): Reservation {
        viewModelScope.launch {
            try {
                val response = userRepository.getReservationById(id)
                if (response.isSuccessful) {
                    reservation.value = response.body()!!
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error", e)
                reservationsRepository.getReservation(id).let {
                    it.collect {
                        reservation.value = it.toReservation()
                        Log.i(
                            "UserViewModel",
                            "reservationsRepository.getReservation(id) " + reservation.value
                        )
                    }
                }
            }
        }.join()
        return reservation.value
    }

    suspend fun loginUser(email: String, password: String) = viewModelScope.launch {
        try {
            Log.d("UserViewModel", "Attempting to login with email: $email")
            val response = userRepository.login(LoginRequestDto(email, password))
            if (response.isSuccessful && response.body()?.token != null) {
                tokenRepository.saveTokenState(response.body()?.token!!)
                _userLoginResult.value = true
                Log.d("UserViewModel", "Login successful, token saved")
                tokenRepository.readTokenState().collect(
                    {
                        Log.d("UserViewModel", "Token read: $it")
                    }
                )
                /* firebaseTokenRepository.readTokenState().collect(
                    {
                        Log.d("UserViewModel", "Firebase token read: $it")
                        FirebaseNotificationsService().sendTokenToServer(it)
                    }
                ) */
            } else {
                _userLoginResult.value = false
                Log.d("UserViewModel", "Login failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            _userLoginResult.value = false
            Log.e("UserViewModel", "Error during login: $e")
        }
    }


    suspend fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(user)
                if (response.isSuccessful) {
                    _userLoginResult.value = true
                    response.body()?.token?.let {
                        tokenRepository.saveTokenState(it)
                    }
                } else {
                    _userLoginResult.value = false
                }
            } catch (e: Exception) {
                _userLoginResult.value = false
            }
        }
    }

    fun getUser(): User {
        viewModelScope.launch {
            try {
                val response = userRepository.getUser()
                if (response.isSuccessful) {
                    user.value = response.body()!!
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error", e)
            }
        }
        return user.value
    }

    fun logoutUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tokenRepository.saveTokenState("")
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        var t = ""
        viewModelScope.launch {
            tokenRepository.readTokenState().collect { token ->
                Log.d("SplashViewModel", "Token: $token")
                t = token
            }
        }
        return t != ""
    }
}