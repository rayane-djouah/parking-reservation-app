package com.example.m_parking.presentation.screen.parking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.m_parking.data.model.Parking
import com.example.m_parking.presentation.viewmodel.ParkingViewModel
import com.example.m_parking.data.model.ReservationRequestBody
import com.example.m_parking.presentation.viewmodel.ReservationCallback
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDialog(
    parking: Parking,
    onDismiss: () -> Unit
) {
    val viewModel: ParkingViewModel = hiltViewModel()

    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedStartTime by remember { mutableStateOf(LocalTime.NOON) }
    var pickedEndTime by remember { mutableStateOf(LocalTime.NOON) }

    val formattedDate = remember(pickedDate) {
        DateTimeFormatter
            .ofPattern("MMM dd yyyy")
            .format(pickedDate)
    }

    val formattedStartTime = remember(pickedStartTime) {
        DateTimeFormatter
            .ofPattern("hh:mm")
            .format(pickedStartTime)
    }

    val formattedEndTime = remember(pickedEndTime) {
        DateTimeFormatter
            .ofPattern("hh:mm")
            .format(pickedEndTime)
    }

    val dateDialogState = rememberMaterialDialogState()
    val startTimeDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    var showUnavailableSlots by remember { mutableStateOf(false) }

    viewModel.setCallback(object : ReservationCallback {
        override fun onReservationSuccess() {
            showUnavailableSlots = false
            onDismiss()
        }

        override fun onReservationFailure() {
            showUnavailableSlots = true
        }
    })

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
    ) {
        Surface(
            color = Color.White,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Booking Details",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Parking Name: ${parking.name}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Location: ${parking.commune}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        dateDialogState.show()
                    }) {
                        Text(text = "Pick date")
                    }
                    Text(text = formattedDate)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        startTimeDialogState.show()
                    }) {
                        Text(text = "Pick start time")
                    }
                    Text(text = formattedStartTime)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        endTimeDialogState.show()
                    }) {
                        Text(text = "Pick end time")
                    }
                    Text(text = formattedEndTime)

                    Spacer(modifier = Modifier.height(25.dp))
                    Button(onClick = {
                        viewModel.reserveParking(
                            parking.id, ReservationRequestBody(
                                1,
                                pickedDate.toString(),
                                pickedStartTime.toString(),
                                pickedEndTime.toString()
                            )
                        )
                    }) {
                        Text(text = "Book Now")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (showUnavailableSlots) {
                        Text("No available slots")
                    }
                    Button(onClick = {
                        onDismiss()
                    }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Date picker dialog
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                dateDialogState.hide()
            }
            negativeButton(text = "Cancel") {
                dateDialogState.hide()
            }
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
        ) {
            pickedDate = it
        }
    }

    MaterialDialog(
        dialogState = startTimeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                startTimeDialogState.hide()
            }
            negativeButton(text = "Cancel") {
                startTimeDialogState.hide()
            }
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Pick a time",
        ) {
            pickedStartTime = it
        }
    }
    MaterialDialog(
        dialogState = endTimeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                endTimeDialogState.hide()
            }
            negativeButton(text = "Cancel") {
                endTimeDialogState.hide()
            }
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Pick a time",
        ) {
            pickedEndTime = it
        }
    }
}
