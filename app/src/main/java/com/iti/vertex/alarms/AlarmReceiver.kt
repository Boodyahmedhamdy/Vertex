package com.iti.vertex.alarms

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.data.sources.local.alarms.AlarmsLocalDataSource
import com.iti.vertex.data.sources.local.db.DatabaseHelper
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity

private const val TAG = "AlarmReceiver"
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive: started")
        when(val action = intent.action) {
            VertexAlarmManager.SEND_ALARM_ACTION -> {
                /*Log.i(TAG, "onReceive: alarm intent received")
                val notificationManager = VertexNotificationManager(context, context.getSystemService(NotificationManager::class.java))
                val alarmEntity = AlarmEntity(
                    city = intent.getStringExtra(VertexAlarmManager.CITY_NAME_KEY) ?: "NONE",
                    startTime = intent.getLongExtra(VertexAlarmManager.ALARM_START_TIME_KEY, 0L)
                )
                notificationManager.showAlarmNotification(alarmEntity)*/
            }
            VertexAlarmManager.CANCEL_ALARM_ACTION -> {
                /*val alarmEntity = AlarmEntity(
                    city = intent.getStringExtra(VertexAlarmManager.CITY_NAME_KEY) ?: "NONE",
                    startTime = intent.getLongExtra(VertexAlarmManager.ALARM_START_TIME_KEY, 0L),
                    id = intent.getIntExtra(VertexAlarmManager.ALARM_ID_KEY, 0),
                    methodStringRes = intent.getIntExtra(VertexAlarmManager.ALARM_METHOD_KEY, 0)
                )*/
//                Log.i(TAG, "onReceive: delete broadcast received for $alarmEntity")
            }
            else -> {
                Log.i(TAG, "onReceive: received action $action")
            }
        }

    }
}