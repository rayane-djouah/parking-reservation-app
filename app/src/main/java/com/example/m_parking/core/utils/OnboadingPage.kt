package com.example.m_parking.core.utils

import androidx.annotation.DrawableRes
import com.example.m_parking.R

sealed class OnboadingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object PageOne : OnboadingPage(
        image = R.drawable.page_one,
        title = "Welcome",
        description = "Welcome to M-Parking! We're thrilled to have you onboard. Get ready to save time and find affordable parking spots with fair prices."
    )

    object PageTwo : OnboadingPage(
        image = R.drawable.page_two,
        title = "Search and Reserve",
        description = "Explore parking spots in your area by entering the address, city, or state. Book your preferred parking spot hassle-free."
    )

    object PageThree : OnboadingPage(
        image = R.drawable.page_three,
        title = "Enjoy Convenience",
        description = "Never worry about losing your parked car again. Easily locate your vehicle in the parking lot with our app."
    )
}
