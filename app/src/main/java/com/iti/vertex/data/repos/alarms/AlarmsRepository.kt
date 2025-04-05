package com.iti.vertex.data.repos.alarms

import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.data.sources.local.alarms.AlarmsLocalDataSource
import com.iti.vertex.data.sources.local.alarms.IAlarmsLocalDataSource

class AlarmsRepository private constructor(
    private val localDataSource: IAlarmsLocalDataSource
) : IAlarmsRepository {

    companion object {
        private var INSTANCE: AlarmsRepository? = null
        fun getInstance(localDataSource: AlarmsLocalDataSource): AlarmsRepository {
            return INSTANCE ?: synchronized(this) {
                AlarmsRepository(localDataSource)
            }
        }
    }


    override fun getAllAlarms() = localDataSource.getAllAlarms()

    override suspend fun insertAlarm(alarmEntity: AlarmEntity) {
        localDataSource.insertAlarm(alarmEntity)
    }

    override suspend fun deleteAlarm(alarmEntity: AlarmEntity) {
        localDataSource.deleteAlarm(alarmEntity)
    }

    override suspend fun deleteAlarmById(id: String) = localDataSource.deleteAlarmById(id)


   override suspend fun getAlarmByID(id: String): AlarmEntity = localDataSource.getAlarmById(id)



}