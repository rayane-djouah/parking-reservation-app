package com.example.m_parking.core.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.example.m_parking.data.api.UserApi
import com.example.m_parking.MainActivity
import com.example.m_parking.R
import com.example.m_parking.core.NOTIFICATION
import com.example.m_parking.data.model.FirebaseToken
import com.example.m_parking.data.repository.AuthTokenStoreRepository
import com.example.m_parking.data.repository.FirebaseTokenStoreRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FirebaseNotificationsService : FirebaseMessagingService() {

    @Inject
    lateinit var userApi: UserApi

    private val tokenRepository: FirebaseTokenStoreRepository
        get() = FirebaseTokenStoreRepository(this)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "New token: $token")

        CoroutineScope(Dispatchers.IO).launch {
            tokenRepository.saveTokenState(token)
        }

        //sendTokenToServer(token)
    }

    fun sendTokenToServer(token: String) {
        val firebaseToken = FirebaseToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = 1 //TODO change user id
                val response = userApi.sendFirebaseToken(firebaseToken)
                if (response.isSuccessful) {
                    Log.d("FCM", "Token sent to server successfully")
                } else {
                    Log.e("FCM", "Failed to send token to server: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Error sending token to server", e)
            }
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let { message ->
            sendNotification(message)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )

        val channelId = NOTIFICATION.CHANNEL_ID
        val chanelName = NOTIFICATION.CHANNEL_NAME

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setSmallIcon(R.drawable.packing_logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, chanelName, IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(Random.nextInt(), notificationBuilder.build())
    }
}