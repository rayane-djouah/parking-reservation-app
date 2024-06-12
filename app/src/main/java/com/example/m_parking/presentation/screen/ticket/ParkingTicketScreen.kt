package com.example.m_parking.presentation.screen.ticket

import QrCodeImage
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.m_parking.data.model.Parking
import com.example.m_parking.data.model.User
import com.example.m_parking.presentation.viewmodel.ParkingViewModel
import com.example.m_parking.presentation.viewmodel.UserViewModel
import com.example.m_parking.data.model.Reservation
import com.example.m_parking.navigation.BottomNavItem
import com.example.m_parking.navigation.BottomNavMenu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingTicketScreen(
    navController: NavController,
    id: String
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val parkingViewModel: ParkingViewModel = hiltViewModel()
    val parking = remember {
        mutableStateOf(
            Parking(
                commune = "...",
                description = "...",
                id = 0,
                latitude = 0.0,
                longitude = 0.0,
                name = "...",
                numSlots = 0,
                photo = "...",
                slotPrice = 0.0
            )
        )
    }
    val reservation = remember {
        mutableStateOf(
            Reservation(
                id = 1,
                parkingId = 1,
                userId = 1,
                date = "../../..",
                entryTime = "00:00",
                exitTime = "00:00",
            )
        )
    }

    LaunchedEffect(key1 = id) {
        val retrievedReservation = userViewModel.getReservationById(id.toInt())
        Log.d("RESERVATION", retrievedReservation.toString())
        val parkingId = retrievedReservation.parkingId
        val retrievedParking = parkingViewModel.getParkingById(parkingId)
        parking.value = retrievedParking
        reservation.value = retrievedReservation
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Parking Ticket",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (isSystemInDarkTheme()) Color.White.copy(.24f) else Color.Black.copy(
                                .14f
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "clear"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(

                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(48.dp),
                containerColor = Color.Transparent
            ) {
                BottomNavMenu(selectedItem = BottomNavItem.TICKET, navController)
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                )
                .padding(horizontal = 10.dp),
        ) {
            QrCode(reservation.value.id, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(20.dp))
            BookingDetails(userViewModel.reservation.value, parking.value, userViewModel.user.value)
        }
    }
}

@Composable
fun QrCode(
    reservationNum: Int,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(.4f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QrCodeImage(content = reservationNum.toString(), size = 200.dp)
    }
}

@Composable
fun BookingDetails(reservation: Reservation, parking: Parking, user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Reservation Number")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reservation.id.toString(),
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Date")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reservation.date,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Time")
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "date")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = reservation.entryTime,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(imageVector = Icons.Default.ArrowRightAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = reservation.exitTime,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Parking Name")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = parking.name,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Parking Location")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = parking.commune,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "User First Name")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.firstName ?: "User First Name",
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "User Last Name")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.lastName ?: "User Last Name",
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "User Phone Number")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.phone ?: "User Phone Number",
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}