package com.iti.vertex.alarms.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.vertex.R
import com.iti.vertex.alarms.alerts.AlertsService
import com.iti.vertex.alarms.components.AlarmPickerBottomSheetContent
import com.iti.vertex.data.sources.local.db.entities.AlarmEntity
import com.iti.vertex.alarms.vm.AlarmsViewModel
import com.iti.vertex.alarms.vm.NotifyingMethod
import com.iti.vertex.alarms.vm.toReadableTime
import com.iti.vertex.favorite.screens.EmptyScreen
import com.iti.vertex.utils.Result

private const val TAG = "AlarmsScreen"

@Composable
fun AlarmsScreen(
    viewModel: AlarmsViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val alarmsState = viewModel.alarmsState.collectAsStateWithLifecycle()
    val showBottomSheetState = viewModel.showBottomSheetState.collectAsStateWithLifecycle()
    val notifyingMethodState = viewModel.notifyingMethodState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            Log.i(TAG, "AlarmsScreen: Permission granted? $isGranted")
            viewModel.updateShowBottomSheetState(true)
        }
    }

    val overLayPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        Log.i(TAG, "AlarmsScreen: accepted to display over other apps")
    }

    AlarmsScreenContent(
        alarmsState = alarmsState.value,
        showBottomSheetState = showBottomSheetState.value,
        onFabButtonClicked = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else viewModel.updateShowBottomSheetState(true)
        },
        onBottomSheetDismissRequest = { viewModel.updateShowBottomSheetState(false) },
        onDateTimeSelected = {
            viewModel.updateShowBottomSheetState(false)
            when(notifyingMethodState.value) {
                NotifyingMethod.NOTIFICATION -> viewModel.scheduleAlarm(it)
                NotifyingMethod.ALARM -> viewModel.scheduleAlert(it)
            }
        },
        modifier = modifier,
        notifyingMethodState = notifyingMethodState.value,
        onMethodClicked = {
            when(it) {
                NotifyingMethod.NOTIFICATION -> viewModel.updateNotifyingMethodState(it)
                NotifyingMethod.ALARM -> {
                    if(!Settings.canDrawOverlays(context)) {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        )
                        overLayPermissionLauncher.launch(intent)
                    } else {
                        viewModel.updateNotifyingMethodState(it)
                    }
                }
            }
        },
        onDeleteAlarmClicked = {
            viewModel.cancelAlarm(it.id)
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
    modifier: Modifier = Modifier,
    onDateTimeSelected: (Long) -> Unit,
    notifyingMethodState: NotifyingMethod,
    onMethodClicked: (NotifyingMethod) -> Unit,
    onDeleteAlarmClicked: (AlarmEntity) -> Unit,
    onAlarmClicked: (AlarmEntity) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.alarms)) }) },
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
                            modifier = Modifier
                                .padding(8.dp)
                                .animateItem()
                        )
                    }
                }
            }
        }

        // bottom sheet picker
        if(showBottomSheetState) {
            ModalBottomSheet(onDismissRequest = onBottomSheetDismissRequest) {
                AlarmPickerBottomSheet(
                    notifyingMethodState = notifyingMethodState,
                    onMethodClicked = onMethodClicked,
                    onDateTimeSelected = onDateTimeSelected,
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
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = alarmEntity.city, style = MaterialTheme.typography.displaySmall)
                Text(text = "From: ${alarmEntity.startTime.toReadableTime()}")
                Text(text = "Method: ${stringResource(alarmEntity.notifyingMethod.displayName)}")
            }
            IconButton(onClick = { onDeleteClicked(alarmEntity) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete alarm", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
fun AlarmPickerBottomSheet(
    notifyingMethodState: NotifyingMethod,
    onMethodClicked: (NotifyingMethod) -> Unit,
    onDateTimeSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    ) {

    AlarmPickerBottomSheetContent(
        modifier = modifier,
        onDateTimeSelected = {
            Log.i(TAG, "AlarmPickerBottomSheet: selected time is ${it}")
            onDateTimeSelected(it)
        },
        notifyingMethodState = notifyingMethodState,
        onMethodClicked = onMethodClicked
    )


}
