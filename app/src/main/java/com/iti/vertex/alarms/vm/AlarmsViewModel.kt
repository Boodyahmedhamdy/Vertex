package com.iti.vertex.alarms.vm


import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.iti.vertex.R
import com.iti.vertex.alarms.workers.AlarmWorker
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
import java.util.UUID
import java.util.concurrent.TimeUnit

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
            val currentCity = settingsRepo.getCurrentLocation().first().cityName
            val id = UUID.randomUUID()
            val alarmEntity = AlarmEntity(
                id = id.toString(),
                startTime = startTime,
                city = currentCity,
                _notifyingMethodState.value
            )

            val workData = workDataOf("ID_KEY" to alarmEntity.id)

            val delay = startTime - System.currentTimeMillis()
            Log.i(TAG, "scheduleAlarm: delay for ${delay / 1000} seconds")

            val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInputData(workData)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setId(id)
                .build()

            alarmsRepository.insertAlarm(alarmEntity)
            Log.i(TAG, "scheduleAlarm: inserted alarm with id ${alarmEntity.id}")
            workManager.enqueue(workRequest)
            Log.i(TAG, "scheduleAlarm: enqueued work with id: ${workRequest.id}")
        }
    }

    fun cancelAlarm(id: String) {
        viewModelScope.launch {
            alarmsRepository.deleteAlarmById(id)
            workManager.cancelWorkById(UUID.fromString(id))
        }
    }
}

fun Long.toReadableTime(locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat("h:mm a", locale) // "h:mm a" for 12-hour format with AM/PM
    return dateFormat.format(Date(this))
}