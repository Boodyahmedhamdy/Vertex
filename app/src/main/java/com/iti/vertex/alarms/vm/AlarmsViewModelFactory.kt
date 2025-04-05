package com.iti.vertex.alarms.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.iti.vertex.alarms.VertexAlarmManager
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.data.repos.alarms.IAlarmsRepository
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.repos.settings.SettingsRepository

@Suppress("UNCHECKED_CAST")
class AlarmsViewModelFactory(
    private val alarmsRepository: IAlarmsRepository,
    private val settingsRepository: ISettingsRepository,
    private val workManager: WorkManager
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmsViewModel(alarmsRepository, settingsRepository, workManager) as T
    }

}