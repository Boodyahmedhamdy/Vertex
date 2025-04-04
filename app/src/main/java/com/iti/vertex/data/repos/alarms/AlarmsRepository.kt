package com.iti.vertex.data.repos.alarms

import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.data.sources.local.alarms.AlarmsLocalDataSource

class AlarmsRepository private constructor(
    private val localDataSource: AlarmsLocalDataSource
) {

    companion object {
        private var INSTANCE: AlarmsRepository? = null
        fun getInstance(localDataSource: AlarmsLocalDataSource): AlarmsRepository {
            return INSTANCE ?: synchronized(this) {
                AlarmsRepository(localDataSource)
            }
        }
    }


    fun getAllAlarms() = localDataSource.getAllAlarms()

    suspend fun insertAlarm(alarmEntity: AlarmEntity) {
        localDataSource.insertAlarm(alarmEntity)
    }

    suspend fun deleteAlarm(alarmEntity: AlarmEntity) {
        localDataSource.deleteAlarm(alarmEntity)
    }

    suspend fun deleteAlarmById(id: String) = localDataSource.deleteAlarmById(id)

   suspend fun getAlarmByStartTime(startTime: Long): AlarmEntity = localDataSource.getAlarmByStartTime(startTime)
   suspend fun getAlarmByID(id: String): AlarmEntity = localDataSource.getAlarmById(id)



}