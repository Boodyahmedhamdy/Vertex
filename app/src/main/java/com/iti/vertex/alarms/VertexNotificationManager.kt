package com.iti.vertex.alarms

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.iti.vertex.MainActivity
import com.iti.vertex.NOTIFICATION_CHANNEL_ID
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity

class VertexNotificationManager(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    fun showAlarmNotification(alarmEntity: AlarmEntity) {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, alarmEntity.startTime.toInt(), intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(alarmEntity.city)
            .setContentText("You have Some Notifications About ${alarmEntity.city}")
            .setSmallIcon(R.drawable.clouds)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(alarmEntity.startTime.toInt(), notification)
    }

}