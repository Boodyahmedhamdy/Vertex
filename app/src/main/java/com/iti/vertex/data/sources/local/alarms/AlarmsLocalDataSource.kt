package com.iti.vertex.data.sources.local.alarms

import com.iti.vertex.data.sources.local.db.AlarmsDao
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmsLocalDataSource(
    private val alarmsDao: AlarmsDao
) : IAlarmsLocalDataSource {

    override fun getAllAlarms() = alarmsDao.getAllAlarms()
    override suspend fun insertAlarm(alarmEntity: AlarmEntity) = withContext(Dispatchers.IO) {
        alarmsDao.insertAlarm(alarmEntity)
    }

    override suspend fun deleteAlarm(alarmEntity: AlarmEntity) = withContext(Dispatchers.IO) {
        alarmsDao.deleteAlarm(alarmEntity)
    }

    override suspend fun getAlarmByStartTime(startTime: Long): AlarmEntity = withContext(Dispatchers.IO) {
        alarmsDao.getAlarmByStartTime(startTime)
    }

    override suspend fun deleteAlarmById(id: String) = withContext(Dispatchers.IO) {
        alarmsDao.deleteAlarmById(id)
    }

    override suspend fun getAlarmById(id: String): AlarmEntity = withContext(Dispatchers.IO) {
        alarmsDao.getAlarmById(id)
    }

}
