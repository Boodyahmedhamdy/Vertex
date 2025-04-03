package com.iti.vertex.data.sources.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.vertex.data.sources.local.db.converters.TypeConverter
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity

@Database(entities = [ForecastEntity::class, AlarmEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class VertexDatabase: RoomDatabase() {
    abstract fun getForecastDao(): ForecastDao
    abstract fun getAlarmsDao(): AlarmsDao

    companion object {
        private var INSTANCE: VertexDatabase? = null

        fun getInstance(context: Context): VertexDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    VertexDatabase::class.java,
                    "vertex_db"
                ).build()
                INSTANCE = db
                db
            }
        }
    }
}