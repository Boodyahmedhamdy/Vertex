package com.iti.vertex.alarms.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.alarms.vm.AlarmsViewModel
import com.iti.vertex.alarms.vm.NotifyingMethod
import com.iti.vertex.alarms.vm.toReadableTime
import com.iti.vertex.favorite.screens.EmptyScreen
import com.iti.vertex.utils.Result

private const val TAG = "AlarmsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(
    viewModel: AlarmsViewModel,
    modifier: Modifier = Modifier,
) {
    val alarmsState = viewModel.alarmsState.collectAsStateWithLifecycle()
    val showBottomSheetState = viewModel.showBottomSheetState.collectAsStateWithLifecycle()
    val startTimeState = viewModel.startTimeState.collectAsStateWithLifecycle()
    val endTimeState = viewModel.endTimeState.collectAsStateWithLifecycle()
    val notifyingMethodState = viewModel.notifyingMethodState.collectAsStateWithLifecycle()

    AlarmsScreenContent(
        alarmsState = alarmsState.value,
        showBottomSheetState = showBottomSheetState.value,
        onFabButtonClicked = { viewModel.updateShowBottomSheetState(true) },
        onBottomSheetDismissRequest = { viewModel.updateShowBottomSheetState(false) },
        onConfirmClicked =  { startTime, endTime ->
            Log.i(TAG, "AlarmsScreen: clicked on confirm")
            viewModel.updateStartTimeState(startTime)
            viewModel.updateEndTimeState(endTime)
            viewModel.updateShowBottomSheetState(false)
//            viewModel.scheduleAlarm()
            viewModel.scheduleAlarmUsingWorkManager()
        },
        modifier = modifier,
        startTimeState = startTimeState.value,
        endTimeState = endTimeState.value,
        notifyingMethodState = notifyingMethodState.value,
        onMethodClicked = { viewModel.updateNotifyingMethodState(it) },
        onDeleteAlarmClicked = {
            viewModel.cancelAlarm(it)
            viewModel.deleteAlarm(it)
        },
        onAlarmClicked = { Log.i(TAG, "AlarmsScreen: clicked on ${it}") },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreenContent(
    alarmsState: Result<out List<AlarmEntity>>,
    showBottomSheetState: Boolean,
    onFabButtonClicked: () -> Unit,
    onBottomSheetDismissRequest: () -> Unit,
    onConfirmClicked: (startTimeState: TimePickerState, endTimeState: TimePickerState) -> Unit,
    modifier: Modifier = Modifier,
    startTimeState: TimePickerState,
    endTimeState: TimePickerState,
    notifyingMethodState: NotifyingMethod,
    onMethodClicked: (NotifyingMethod) -> Unit,
    onDeleteAlarmClicked: (AlarmEntity) -> Unit,
    onAlarmClicked: (AlarmEntity) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { onFabButtonClicked() }) { Icon(Icons.Outlined.Add, contentDescription = "Add Alarm") }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->

        when(alarmsState) {
            Result.Loading -> Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is Result.Error -> { EmptyScreen(drawableRes = R.drawable.baseline_broken_image_24, alarmsState.message, modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) }

            is Result.Success -> {
                if(alarmsState.data.isEmpty()) EmptyScreen(drawableRes = R.drawable.weather_air_pressure, "No Alarms Yet", modifier = Modifier.fillMaxSize())
                else LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(alarmsState.data, key = {it.id}) {item ->
                        AlarmCard(
                            alarmEntity = item,
                            onDeleteClicked = onDeleteAlarmClicked,
                            onItemClicked = onAlarmClicked,
                            modifier = Modifier.padding(8.dp).animateItem()
                        )
                    }
                }
            }
        }

        // bottom sheet picker
        if(showBottomSheetState) {
            ModalBottomSheet(onDismissRequest = onBottomSheetDismissRequest) {
                AlarmPickerBottomSheet(
                    onCancelClicked = { onBottomSheetDismissRequest() },
                    onConfirmClicked = { start, end -> onConfirmClicked(start, end) },
                    startTimeState = startTimeState,
                    endTimeState = endTimeState,
                    notifyingMethodState = notifyingMethodState,
                    onMethodClicked = onMethodClicked
                )
            }
        }
    }
}

@Composable
fun AlarmCard(
    alarmEntity: AlarmEntity,
    onDeleteClicked: (AlarmEntity) -> Unit,
    onItemClicked: (AlarmEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, onClick = { onItemClicked(alarmEntity) }) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = alarmEntity.city, style = MaterialTheme.typography.displaySmall)
                Text(text = "From: ${alarmEntity.startTime.toReadableTime()} To ${alarmEntity.endTime.toReadableTime()}")
            }
            IconButton(onClick = { onDeleteClicked(alarmEntity) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete alarm", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmPickerBottomSheet(
    startTimeState: TimePickerState,
    endTimeState: TimePickerState,
    onCancelClicked: () -> Unit,
    onConfirmClicked: (startTimeState: TimePickerState, endTimeState: TimePickerState) -> Unit,
    notifyingMethodState: NotifyingMethod,
    onMethodClicked: (NotifyingMethod) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Start Time", style = MaterialTheme.typography.titleMedium)
        TimeInput(state = startTimeState)
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp))


        Text(text = "End Time", style = MaterialTheme.typography.titleMedium)
        TimeInput(state = endTimeState)
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp))


        Text(text = "Notification Method", style = MaterialTheme.typography.titleMedium)
        Row {
            NotifyingMethod.entries.forEach { method ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = notifyingMethodState == method,
                        onClick = { onMethodClicked(method) },
                    )
                    Text(text = method.name)
                }
            }
        }

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedButton(onClick = { onCancelClicked() }) { Text(text = "Cancel") }
            Button(onClick = { onConfirmClicked(startTimeState, endTimeState) }) { Text(text = "Confirm") }
        }

    }






}
