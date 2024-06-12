package com.example.m_parking

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.m_parking.navigation.NavGraph
import com.example.m_parking.navigation.Screens
import com.example.m_parking.presentation.screen.onboarding.SplashViewModel
import com.example.m_parking.ui.theme.MParkingTheme
import com.example.m_parking.utils.checkForPermission
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var splashViewModel: SplashViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using ViewModelProvider to retrieve the ViewModel
        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }
        setContent {
            MParkingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var screen = splashViewModel.getStartDestination()
                    var hasLocationPermission by remember { mutableStateOf(checkForPermission(this)) }
                    if (!hasLocationPermission) {
                        screen = Screens.LocationScreen.route
                    }
                    NavGraph(startDestination = screen)
                }
            }
        }
    }
}

