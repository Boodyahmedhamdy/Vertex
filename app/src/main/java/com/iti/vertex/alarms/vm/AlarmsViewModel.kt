package com.iti.vertex.alarms.vm


import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.iti.vertex.alarms.MyWorker
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.alarms.VertexAlarmManager
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val TAG = "AlarmsViewModel"

enum class NotifyingMethod { NOTIFICATION, ALARM }

@OptIn(ExperimentalMaterial3Api::class)
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

    private val _startTimeState = MutableStateFlow(TimePickerState(initialMinute = Calendar.MINUTE, initialHour = Calendar.HOUR_OF_DAY, is24Hour = false))
    val startTimeState = _startTimeState.asStateFlow()
    fun updateStartTimeState(timePickerState: TimePickerState) = _startTimeState.update { timePickerState }
    fun isStartTimeStateValid(timePickerState: TimePickerState) : Boolean {
        return timePickerState.toMillis() >= System.currentTimeMillis()
    }

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
            val startTime = _startTimeState.value.toMillis()
            val endTime = _endTimeState.value.toMillis()
            val city = settingsRepo.getCurrentLocation().first().cityName
            val alarmEntity = AlarmEntity(startTime = startTime, endTime = endTime, city = city)
            insertAlarm(alarmEntity)
            vertexAlarmManager.schedule(alarmEntity)
        }
    }

    fun scheduleAlarmUsingWorkManager() {
        viewModelScope.launch {

            val startTime = _startTimeState.value.toMillis()
            val endTime = _endTimeState.value.toMillis()
            val city = settingsRepo.getCurrentLocation().first().cityName
            val alarmEntity = AlarmEntity(startTime = startTime, endTime = endTime, city = city)

            insertAlarm(alarmEntity)

            val workData = workDataOf(
                "id" to alarmEntity.id,
                "startTime" to alarmEntity.startTime,
                "endTime" to alarmEntity.endTime,
                "city" to alarmEntity.city,
            )

            val delay = _startTimeState.value.toMillis() - System.currentTimeMillis()
            val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(workData)
                .build()

            workManager.enqueue(workRequest)

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

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toMillis(calendar: Calendar = Calendar.getInstance()): Long {
    // 1. Get the hour and minute from the TimePickerState.
    val hour = this.hour
    val minute = this.minute

    // 2. Set the hour and minute in the provided Calendar instance.
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0) // Ensure seconds are zero.
    calendar.set(Calendar.MILLISECOND, 0) // Ensure milliseconds are zero.

    // 3. Return the time in milliseconds.
    return calendar.timeInMillis
}

fun Long.toReadableTime(locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat("h:mm a", locale) // "h:mm a" for 12-hour format with AM/PM
    return dateFormat.format(Date(this))
}