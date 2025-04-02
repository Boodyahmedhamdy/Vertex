package com.iti.vertex.alarms.vm

import com.iti.vertex.alarms.AlarmEntity
import com.iti.vertex.alarms.AlarmsDao
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
