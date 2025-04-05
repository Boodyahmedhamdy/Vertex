package com.iti.vertex.data.sources.local.alarms

import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface IAlarmsLocalDataSource {
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    suspend fun insertAlarm(alarmEntity: AlarmEntity)

    suspend fun deleteAlarm(alarmEntity: AlarmEntity)

    suspend fun getAlarmByStartTime(startTime: Long): AlarmEntity

    suspend fun deleteAlarmById(id: String)

    suspend fun getAlarmById(id: String): AlarmEntity
}