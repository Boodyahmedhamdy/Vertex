package com.iti.vertex.alarms

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.iti.vertex.data.dtos.City
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: Long,
    val endTime: Long,
    val city: String,
)


@Dao
interface AlarmsDao {
    @Query("select * from alarms")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("select * from alarms where id = :id")
    suspend fun getAlarmById(id: Int): AlarmEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmEntity: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarmEntity: AlarmEntity)
}