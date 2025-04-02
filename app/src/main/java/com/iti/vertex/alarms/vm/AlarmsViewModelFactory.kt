package com.iti.vertex.alarms.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.alarms.VertexAlarmManager
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.data.repos.settings.SettingsRepository

@Suppress("UNCHECKED_CAST")
class AlarmsViewModelFactory(
    private val alarmManager: VertexAlarmManager,
    private val alarmsRepository: AlarmsRepository,
    private val settingsRepository: SettingsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmsViewModel(alarmManager, alarmsRepository, settingsRepository) as T
    }

}