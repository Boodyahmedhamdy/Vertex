package com.iti.vertex.alarms.vm


import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.iti.vertex.R
import com.iti.vertex.alarms.VertexAlarmManager
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "AlarmsViewModel"

enum class NotifyingMethod(@StringRes val displayName: Int) {
    NOTIFICATION(R.string.notification),
    ALARM(R.string.alarm)
}


class AlarmsViewModel(
    private val vertexAlarmManager: VertexAlarmManager,
    private val alarmsRepository: AlarmsRepository,
    private val settingsRepo: ISettingsRepository,
    private val workManager: WorkManager
): ViewModel() {

    private val _alarmsState: MutableStateFlow<Result<out List<AlarmEntity>>> = MutableStateFlow(Result.Loading)
    val alarmsState = _alarmsState.asStateFlow()

    private val _showBottomSheetState = MutableStateFlow(false)
    val showBottomSheetState = _showBottomSheetState.asStateFlow()
    fun updateShowBottomSheetState(showBottomSheet: Boolean) = _showBottomSheetState.update { showBottomSheet }

    private val _notifyingMethodState = MutableStateFlow(NotifyingMethod.NOTIFICATION)
    val notifyingMethodState = _notifyingMethodState.asStateFlow()
    fun updateNotifyingMethodState(notifyingMethod: NotifyingMethod) = _notifyingMethodState.update { notifyingMethod }


    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            alarmsRepository.getAllAlarms().collect { alarmsList ->
                _alarmsState.update {
                    Result.Success(alarmsList)
                }
            }
        }
    }

    fun scheduleAlarm(startTime: Long) {
        viewModelScope.launch {
            Log.i(TAG, "scheduleAlarm: started")

            val city = settingsRepo.getCurrentLocation().first().cityName
            val alarmEntity = AlarmEntity(startTime = startTime, methodStringRes = _notifyingMethodState.value.displayName, city = city)
            insertAlarm(alarmEntity)
            vertexAlarmManager.schedule(alarmEntity)
        }
    }


    private fun insertAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            alarmsRepository.insertAlarm(alarmEntity)
        }
    }

    fun cancelAlarm(alarmEntity: AlarmEntity) {
        vertexAlarmManager.cancel(alarmEntity)
    }

    fun deleteAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch { alarmsRepository.deleteAlarm(alarmEntity) }
    }
}

fun Long.toReadableTime(locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat("h:mm a", locale) // "h:mm a" for 12-hour format with AM/PM
    return dateFormat.format(Date(this))
}