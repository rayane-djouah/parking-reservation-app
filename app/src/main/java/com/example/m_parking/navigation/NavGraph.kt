package com.example.m_parking.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.m_parking.presentation.screen.home.HomeScreen
import com.example.m_parking.presentation.screen.ticket.ParkingTicketScreen
import com.example.m_parking.presentation.screen.ticket.TicketScreen
import com.example.m_parking.presentation.screen.auth.LoginScreen
import com.example.m_parking.presentation.screen.auth.RegistrationScreen
import com.example.m_parking.presentation.screen.map.LocationPermissionScreen
import com.example.m_parking.presentation.screen.onboarding.WelcomeScreen
import com.example.m_parking.presentation.screen.parking.ParkingListScreen
import com.example.m_parking.presentation.screen.parking.ParkingScreen
import com.example.m_parking.presentation.screen.profile.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    parkingId: String? = null
    //viewModel: AuthViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screens.RegisterScreen.route) {
            RegistrationScreen(navController)
        }
        composable(Screens.TicketScreen.route) {
            TicketScreen(navController)
        }
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(Screens.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }
        composable(route = "${Screens.ParkingTicketScreen.route}/{reservationId}") { backStackEntry ->
            val args = requireNotNull(backStackEntry.arguments)
            val id = args.getString("reservationId")
            id?.let {
                ParkingTicketScreen(navController, it)
            }
        }
        composable(Screens.ParkingListScreen.route) {
            ParkingListScreen(navController)
        }
        composable(route = "${Screens.ParkingScreen.route}/{parkingId}") { backStackEntry ->
            val args = requireNotNull(backStackEntry.arguments)
            val id = args.getString("parkingId")
            id?.let {
                ParkingScreen(navController, it)
            }
        }
        composable(Screens.LocationScreen.route) {
            LocationPermissionScreen(navController)
        }

    }
}
