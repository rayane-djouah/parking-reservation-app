package com.example.m_parking.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m_parking.data.model.User
import com.example.m_parking.data.repository.AuthTokenStoreRepository
import com.example.m_parking.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: AuthTokenStoreRepository
) :
    ViewModel() {
    val firstname: MutableState<String> = mutableStateOf("")
    val lastName: MutableState<String> = mutableStateOf("")
    val email: MutableState<String> = mutableStateOf("")
    val phoneNumber: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val message: MutableState<String> = mutableStateOf("")
    val regSuccess = MutableLiveData<Boolean>()

    suspend fun register() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    userRepository.register(
                        User(
                            firstName = firstname.value,
                            lastName = lastName.value,
                            email = email.value,
                            phone = phoneNumber.value,
                            password = password.value
                        )

                    )
                }
                if (response.isSuccessful) {
                    response.body()?.token?.let {
                        tokenRepository.saveTokenState(it)
                    }
                    regSuccess.value = true
                    message.value = "success"

                } else {
                    regSuccess.value = false
                    message.value = "error"
                }
            } catch (e: Exception) {
                regSuccess.value = false
                message.value = "error ${e.message}"
            }
        }
    }
}
