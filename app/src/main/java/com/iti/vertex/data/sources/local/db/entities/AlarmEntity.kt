package com.iti.vertex.data.sources.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iti.vertex.alarms.vm.NotifyingMethod

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String,
    val startTime: Long,
    val city: String,
    val notifyingMethod: NotifyingMethod
)
