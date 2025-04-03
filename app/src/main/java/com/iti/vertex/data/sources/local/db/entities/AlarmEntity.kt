package com.iti.vertex.data.sources.local.db.entities

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: Long,
    val city: String,
    @StringRes val methodStringRes: Int = 0
)
