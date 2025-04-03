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

        val soundUri = Uri.parse("android.resource://$packageName/${R.raw.gym}")
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        channel.setSound(soundUri, audioAttributes)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}