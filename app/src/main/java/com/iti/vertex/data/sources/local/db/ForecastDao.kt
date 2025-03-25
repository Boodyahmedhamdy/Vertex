package com.iti.vertex.data.sources.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecastEntity: ForecastEntity)

    @Delete
    suspend fun deleteForecast(forecastEntity: ForecastEntity)

    @Query("SELECT * FROM FORECASTENTITY")
    fun getAllForecast(): Flow<List<ForecastEntity>>

}