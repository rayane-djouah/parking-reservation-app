package com.example.m_parking.presentation.screen.auth


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.m_parking.presentation.viewmodel.UserViewModel
import com.example.m_parking.navigation.Screens

@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val loginResult by viewModel.userLoginResult.collectAsState()

    val userViewModel: UserViewModel = hiltViewModel()


    LaunchedEffect(key1 = null) {
        if (userViewModel.isUserLoggedIn()) {
            navController.navigate(Screens.HomeScreen.route)
        }
    }

    Log.d("LoginScreen", "Rendering with isLoading: $isLoading, loginResult: $loginResult")

    // Effect for handling login result
    LaunchedEffect(key1 = isLoading) {
        if (isLoading) {
            Log.d("LoginScreen", "Initiating login for email: $email")
            viewModel.loginUser(email, password)  // This call is now within a coroutine scope
            // Reset isLoading after the attempt to avoid re-triggering without user action
            isLoading = false
        }
    }

    // Observing login result changes
    LaunchedEffect(loginResult) {
        Log.d("LoginScreen", "Login result received: $loginResult")
        if (loginResult) {
            Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()
            navController.navigate(Screens.HomeScreen.route)
        } else if (!loginResult && email.isNotEmpty() && password.isNotEmpty()) {
            Toast.makeText(context, "Login failed. Check credentials.", Toast.LENGTH_LONG).show()
        }
    }

    // Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
                Log.d("LoginScreen", "Email updated: $email")
            },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
                Log.d("LoginScreen", "Password updated")
            },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                isLoading = true
                Log.d("LoginScreen", "Login triggered from keyboard")
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                Log.d("LoginScreen", "Login button clicked")
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text("Log In")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        androidx.compose.material3.TextButton(
            onClick = { navController.navigate(Screens.RegisterScreen.route) },
            modifier = Modifier
        ) {
            androidx.compose.material3.Text(
                "Don't have an account? Sign up",
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            )
        }
    }
}
