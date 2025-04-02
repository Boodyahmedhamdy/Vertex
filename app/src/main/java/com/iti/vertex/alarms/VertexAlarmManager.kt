package com.iti.vertex.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import kotlin.math.log

private const val TAG = "VertexAlarmManager"

class VertexAlarmManager(
    private val context: Context,
    private val alarmManager: AlarmManager,
) {

    companion object {
        const val CITY_NAME_KEY = "CITY_NAME_KEY"
        const val ALARM_START_TIME_KEY = "ALARM_START_TIME_KEY"
        const val ALARM_END_TIME_KEY = "ALARM_END_TIME_KEY"

        const val SEND_ALARM_ACTION = "SEND_ALARM_ACTION"
    }

    @SuppressLint("MissingPermission")
    fun schedule(alarmEntity: AlarmEntity) {
        Log.i(TAG, "schedule: started")
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = SEND_ALARM_ACTION

            putExtra(CITY_NAME_KEY, alarmEntity.city)
            putExtra(ALARM_START_TIME_KEY, alarmEntity.startTime)
            putExtra(ALARM_END_TIME_KEY, alarmEntity.endTime)
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
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmEntity.id, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.i(TAG, "cancel: finished ")
    }


}