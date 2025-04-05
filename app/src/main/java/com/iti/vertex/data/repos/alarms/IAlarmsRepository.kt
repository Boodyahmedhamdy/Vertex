package com.iti.vertex.data.repos.alarms

import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface IAlarmsRepository {
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    suspend fun insertAlarm(alarmEntity: AlarmEntity)

    suspend fun deleteAlarm(alarmEntity: AlarmEntity)

    suspend fun deleteAlarmById(id: String)

    suspend fun getAlarmByID(id: String): AlarmEntity
}