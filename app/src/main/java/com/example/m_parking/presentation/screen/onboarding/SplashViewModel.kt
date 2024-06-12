package com.example.m_parking.presentation.screen.onboarding

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m_parking.data.repository.AuthTokenStoreRepository
import com.example.m_parking.data.repository.DataStoreRepository
import com.example.m_parking.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository,
    private val tokenRepository: AuthTokenStoreRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    fun getStartDestination(): String {
        var d = ""
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                Log.d("readOnBoardingState", "completed: $completed")
                if (!completed) {
                    Log.d("readOnBoardingState hhh", "completed: $completed")
                    d = Screens.WelcomeScreen.route
                }
            }
        }
        viewModelScope.launch {
            tokenRepository.readTokenState().collect { token ->
                Log.d("SplashViewModel", "Token: $token")
                if (token != "") {
                    d = Screens.HomeScreen.route
                } else {
                    if (d != Screens.WelcomeScreen.route) {
                        d = Screens.LoginScreen.route
                    }
                }
            }
            _isLoading.value = false
        }
        return d
    }

}