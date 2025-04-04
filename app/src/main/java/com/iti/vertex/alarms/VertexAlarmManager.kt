package com.iti.vertex.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import java.util.Calendar
import kotlin.math.log

private const val TAG = "VertexAlarmManager"

class VertexAlarmManager(
    private val context: Context,
    private val alarmManager: AlarmManager,
) {

    companion object {
        const val ALARM_ID_KEY = "ALARM_ID_KEY"
        const val CITY_NAME_KEY = "CITY_NAME_KEY"
        const val ALARM_START_TIME_KEY = "ALARM_START_TIME_KEY"
        const val ALARM_METHOD_KEY = "ALARM_METHOD_KEY"

        const val SEND_ALARM_ACTION = "SEND_ALARM_ACTION"
        const val CANCEL_ALARM_ACTION = "CANCEL_ALARM_ACTION"
    }

//    @SuppressLint("MissingPermission")
 /*   fun schedule(alarmEntity: AlarmEntity) {
        Log.i(TAG, "schedule: started")
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = SEND_ALARM_ACTION

            putExtra(ALARM_ID_KEY, alarmEntity.id)
            putExtra(CITY_NAME_KEY, alarmEntity.city)
            putExtra(ALARM_START_TIME_KEY, alarmEntity.startTime)
            putExtra(ALARM_METHOD_KEY, alarmEntity.methodStringRes)
        }
        Log.i(TAG, "schedule: created intent")

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmEntity.id *//*alarmEntity.startTime.toInt()*//*, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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
        val intent = Intent(context, AlarmReceiver::class.java).apply {

            putExtra(ALARM_ID_KEY, alarmEntity.id)
            putExtra(CITY_NAME_KEY, alarmEntity.city)
            putExtra(ALARM_START_TIME_KEY, alarmEntity.startTime)
            putExtra(ALARM_METHOD_KEY, alarmEntity.methodStringRes)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmEntity.id *//*alarmEntity.startTime.toInt()*//*, intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.i(TAG, "cancel: finished ")
    }*/
}

