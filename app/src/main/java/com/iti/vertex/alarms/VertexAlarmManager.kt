package com.iti.vertex.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "VertexAlarmManager"

class VertexAlarmManager(
    private val context: Context,
    private val alarmManager: AlarmManager,
) {

    @SuppressLint("MissingPermission")
    fun schedule(alarmEntity: AlarmEntity) {
        Log.i(TAG, "schedule: started")
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("CITY_NAME", alarmEntity.city)
            action = "ALARM"
        }
        Log.i(TAG, "schedule: created intent")

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmEntity.id, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.i(TAG, "schedule: created pending intent")


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmEntity.startTime,
            pendingIntent
        )
        Log.i(TAG, "schedule: set exact alarm")

    }

    fun cancel(alarmEntity: AlarmEntity) {
        Log.i(TAG, "cancel: started")
    }


}