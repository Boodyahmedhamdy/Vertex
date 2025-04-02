package com.iti.vertex.alarms.vm

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.alarms.AlarmEntity
import com.iti.vertex.alarms.VertexAlarmManager
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "AlarmsViewModel"

enum class NotifyingMethod { NOTIFICATION, ALARM }

@OptIn(ExperimentalMaterial3Api::class)
class AlarmsViewModel(
    private val vertexAlarmManager: VertexAlarmManager,
    private val alarmsRepository: AlarmsRepository,
    private val settingsRepo: ISettingsRepository
): ViewModel() {

    private val _alarmsState: MutableStateFlow<Result<out List<AlarmEntity>>> = MutableStateFlow(Result.Loading)
    val alarmsState = _alarmsState.asStateFlow()

    private val _showBottomSheetState = MutableStateFlow(false)
    val showBottomSheetState = _showBottomSheetState.asStateFlow()
    fun updateShowBottomSheetState(showBottomSheet: Boolean) = _showBottomSheetState.update { showBottomSheet }

    private val _startTimeState = MutableStateFlow(TimePickerState(initialMinute = Calendar.MINUTE, initialHour = Calendar.HOUR_OF_DAY, is24Hour = false))
    val startTimeState = _startTimeState.asStateFlow()
    fun updateStartTimeState(timePickerState: TimePickerState) = _startTimeState.update { timePickerState }

    private val _endTimeState = MutableStateFlow(TimePickerState(initialMinute = Calendar.MINUTE, initialHour = Calendar.HOUR_OF_DAY, is24Hour = false))
    val endTimeState = _endTimeState.asStateFlow()
    fun updateEndTimeState(timePickerState: TimePickerState) = _endTimeState.update { timePickerState }

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

    fun scheduleAlarm() {
        viewModelScope.launch {
            Log.i(TAG, "scheduleAlarm: started")
            val startTime = _startTimeState.value.hour.toLong()
            val endTime = _endTimeState.value.hour.toLong()
            val city = settingsRepo.getCurrentLocation().first().cityName
            val alarmEntity = AlarmEntity(startTime = startTime, endTime = endTime, city = city)
            insertAlarm(alarmEntity)
            vertexAlarmManager.schedule(alarmEntity)
        }
    }

    private fun insertAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            alarmsRepository.insertAlarm(alarmEntity)
        }
    }

    private fun deleteAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch { alarmsRepository.deleteAlarm(alarmEntity) }
    }



}