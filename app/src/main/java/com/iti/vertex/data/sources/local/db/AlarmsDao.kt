package com.iti.vertex.data.sources.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmsDao {
    @Query("select * from alarms")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("select * from alarms where id = :id")
    suspend fun getAlarmById(id: String): AlarmEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmEntity: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarmEntity: AlarmEntity)

    @Query("delete from alarms where id = :id")
    suspend fun deleteAlarmById(id: String)

    @Query("select * from alarms where startTime = :startTime limit 1")
    suspend fun getAlarmByStartTime(startTime: Long): AlarmEntity
}