package com.example.m_parking.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens("Login_screen")
    object RegisterScreen : Screens("Register_screen")

    object HomeScreen : Screens("Home_screen")
    object ProfileScreen : Screens("Profile_screen")
    object TicketScreen : Screens("Ticket_Screen")
    object ParkingListScreen : Screens("Parking_List_Screen")
    object ParkingScreen : Screens("Parking_Screen")
    object WelcomeScreen : Screens("Welcome_Screen")
    object ParkingTicketScreen : Screens("Parking_ticket_Screen")
    object ProfileImageScreen : Screens("Profile_image_Screen")
    object LocationScreen : Screens("Location_Screen")
}