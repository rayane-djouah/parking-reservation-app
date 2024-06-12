package com.example.m_parking.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AirplaneTicket
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

enum class BottomNavItem(val icon: ImageVector, val route: String) {
    HOME(Icons.Outlined.Home, Screens.HomeScreen.route),
    PARKINGS(Icons.Outlined.Search, Screens.ParkingListScreen.route),
    TICKET(Icons.Outlined.AirplaneTicket, Screens.TicketScreen.route),
    PROFILE(Icons.Outlined.AccountCircle, Screens.ProfileScreen.route),
}

@Composable
fun BottomNavMenu(
    selectedItem: BottomNavItem,
    navController: NavController
) {
    Row {
        for (item in BottomNavItem.values()) {
            Image(
                imageVector = item.icon,
                contentDescription = "Image Item",
                modifier = Modifier
                    .size(33.dp)
                    .weight(1f)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(item.route)
                    },
                colorFilter = if (item == selectedItem)
                    ColorFilter.tint(Color.Cyan) else
                    ColorFilter.tint(Color.DarkGray)
            )
        }
    }
}