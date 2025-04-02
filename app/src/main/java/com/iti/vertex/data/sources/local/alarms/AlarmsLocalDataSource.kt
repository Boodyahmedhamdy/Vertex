package com.iti.vertex.data.sources.local.alarms

import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.data.sources.local.db.entities.AlarmsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmsLocalDataSource(
    private val alarmsDao: AlarmsDao
) {

    fun getAllAlarms() = alarmsDao.getAllAlarms()
    suspend fun insertAlarm(alarmEntity: AlarmEntity) = withContext(Dispatchers.IO) {
        alarmsDao.insertAlarm(alarmEntity)
    }

    suspend fun deleteAlarm(alarmEntity: AlarmEntity) = withContext(Dispatchers.IO) {
        alarmsDao.deleteAlarm(alarmEntity)
    }

}
