package com.iti.vertex.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "AlarmReceiver"
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive: started")
        val action  = intent.action ?: "NONE"
        when(action) {
            "ALARM" -> {
                Log.i(TAG, "onReceive: alarm intent received")
            }
            else -> {
                Log.i(TAG, "onReceive: received action $action")
            }
        }

    }
}