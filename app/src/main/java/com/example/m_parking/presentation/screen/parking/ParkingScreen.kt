package com.example.m_parking.presentation.screen.parking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.m_parking.data.model.Parking
import com.example.m_parking.presentation.viewmodel.ParkingViewModel
import com.example.m_parking.navigation.BottomNavItem
import com.example.m_parking.navigation.BottomNavMenu


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingScreen(
    navController: NavController,
    id: String
) {
    val viewModel: ParkingViewModel = hiltViewModel()
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

    LaunchedEffect(key1 = id) {
        val parkingId = id.toIntOrNull()
        if (parkingId != null) {
            val retrievedParking = viewModel.getParkingById(parkingId)
            parking.value = retrievedParking
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Parking Details",
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
                BottomNavMenu(selectedItem = BottomNavItem.PARKINGS, navController)
            }
        }) { paddingValues ->
        parking.let { parkingDetails ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        top = paddingValues.calculateTopPadding()
                    )
                    .padding(horizontal = 10.dp),
            ) {
                ParkingPhoto(
                    parkingDetails.value,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                ParkingDetails(parkingDetails.value)
                Spacer(modifier = Modifier.height(10.dp))
                Actions(navController, parking.value)
            }
        }
    }
}

@Composable
fun ParkingPhoto(
    parking: Parking,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = parking.photo,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun ParkingDetails(parking: Parking) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Name")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = parking.name,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Capacity")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = parking.numSlots.toString(),
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
            Text(text = "City")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = parking.commune,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Price per hour")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${parking.slotPrice} DA / hour",
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
            Text(text = "Description")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = parking.description ?: "No description",
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Actions(navController: NavController, parking: Parking) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.Cyan
            ),
        ) {
            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Book a slot")
            Text(
                text = "Book a slot",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    if (showDialog) {
        BookingDialog(
            parking = parking,
            onDismiss = { showDialog = false }
        )
    }
}
