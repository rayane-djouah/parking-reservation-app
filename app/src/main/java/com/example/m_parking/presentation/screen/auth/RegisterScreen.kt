package com.example.m_parking.presentation.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.m_parking.navigation.Screens
import com.example.m_parking.presentation.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(navController: NavController) {
    val viewModel: RegistrationViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.regSuccess.observe(lifecycleOwner, Observer { shouldNavigate ->
        if (shouldNavigate) {
            navController.navigate(Screens.ParkingListScreen.route)
        }
    })

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            value = viewModel.firstname.value,
            onValueChange = { viewModel.firstname.value = it },
            label = { Text("Name") }
        )
        TextField(
            value = viewModel.lastName.value,
            onValueChange = { viewModel.lastName.value = it },
            label = { Text("Last Name") }
        )
        TextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Email") }
        )
        TextField(
            value = viewModel.phoneNumber.value,
            onValueChange = { viewModel.phoneNumber.value = it },
            label = { Text("Phone Number") }
        )
        TextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
        )
        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
        Text(text = viewModel.message.value)
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(Screens.LoginScreen.route) },
            modifier = Modifier
        ) {
            Text("Already have an account? Login", color = MaterialTheme.colorScheme.primary)
        }
    }
}
