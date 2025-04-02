package com.iti.vertex

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri


const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
class VertexApp: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Weather Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used to Notify user by forecast alarms"

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}