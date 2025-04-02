package com.iti.vertex.alarms.vm

import com.iti.vertex.alarms.AlarmEntity
import com.iti.vertex.alarms.AlarmReceiver

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
}