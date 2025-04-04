package com.iti.vertex.alarms.workers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.vertex.alarms.alerts.AlertsService
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.data.sources.local.alarms.AlarmsLocalDataSource
import com.iti.vertex.data.sources.local.db.DatabaseHelper
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity

private const val TAG = "AlertWorker"
class AlertWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {
    private val alarmsRepository = AlarmsRepository.getInstance(
        localDataSource = AlarmsLocalDataSource(DatabaseHelper.getAlarmsDao(appContext))
    )

    override suspend fun doWork(): Result {
        return try {
            Log.i(TAG, "doWork: started")
            val id = inputData.getString("ID_KEY") ?: ""
            val entity: AlarmEntity = alarmsRepository.getAlarmByID(id)
            Log.i(TAG, "doWork: got temp alarm: $entity")


            val intent = Intent(appContext, AlertsService::class.java).apply {
                putExtra("DESC", "You Have Notification about ${entity.city}")
            }
            appContext.startService(intent)

            // delete after ringing
            alarmsRepository.deleteAlarm(entity)
            Log.i(TAG, "doWork: deleted alarm with id ${entity.id}")

            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }

    }
}