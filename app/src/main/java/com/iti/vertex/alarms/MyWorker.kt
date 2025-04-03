package com.iti.vertex.alarms

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity

class MyWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val manager = VertexNotificationManager(appContext, appContext.getSystemService(NotificationManager::class.java))

        val entity = AlarmEntity(
            id = inputData.getInt("id", 1),
            startTime = inputData.getLong("startTime", 1L),
            city = inputData.getString("city") ?: "NONE"
        )

        manager.showAlarmNotification(entity)
        return Result.success()
    }
}