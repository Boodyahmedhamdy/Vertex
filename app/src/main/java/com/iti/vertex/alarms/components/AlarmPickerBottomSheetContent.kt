package com.iti.vertex.alarms.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.vertex.R
import com.iti.vertex.alarms.vm.NotifyingMethod
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AlarmPickerBottomSheetContent(
    modifier: Modifier = Modifier,
    onDateTimeSelected: (Long) -> Unit,
    notifyingMethodState: NotifyingMethod,
    onMethodClicked: (NotifyingMethod) -> Unit,
) {
    var selectedDateLabel by remember { mutableStateOf("") }
    var selectedTimeLabel by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    val showSaveButton = selectedDate != null && selectedTime != null
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(text = stringResource(R.string.choose_suitable_time_to_set_alarm), style = MaterialTheme.typography.titleLarge)
        HorizontalDivider(Modifier.fillMaxWidth())

        Text(text = "$selectedDateLabel $selectedTimeLabel")

        Text(text = stringResource(R.string.select_date))
        OutlinedButton(modifier = Modifier.fillMaxWidth(),  onClick = {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // Month is 0-indexed in DatePickerDialog
                    selectedDateLabel = "${selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
                },
                LocalDate.now().year,
                LocalDate.now().monthValue - 1, // Month is 0-indexed in DatePickerDialog
                LocalDate.now().dayOfMonth
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000 //Disables past dates.
            datePickerDialog.show()
        }) {
            Text(text = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: stringResource(R.string.select_date))
        }

        HorizontalDivider(Modifier.fillMaxWidth())


        Text(text = stringResource(R.string.select_time))
        OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    selectedTime = LocalTime.of(hourOfDay, minute)
                    selectedTimeLabel = "${selectedTime?.format(DateTimeFormatter.ISO_LOCAL_TIME)}"
                },
                LocalTime.now().hour,
                LocalTime.now().minute,
                false
            )
            timePickerDialog.show()
        }) {
            Text(text = selectedTime?.format(DateTimeFormatter.ISO_LOCAL_TIME) ?: stringResource(R.string.select_time))
        }
        HorizontalDivider(Modifier.fillMaxWidth())

        Text(text = stringResource(R.string.notification_method), style = MaterialTheme.typography.titleMedium)
        Row {
            NotifyingMethod.entries.forEach { method ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = notifyingMethodState == method,
                        onClick = { onMethodClicked(method) },
                    )
                    Text(text = stringResource(method.displayName))
                }
            }
        }

        // save button
        if (showSaveButton) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                val zoneDateTime = ZonedDateTime.of(selectedDate, selectedTime, ZoneId.systemDefault())
                val millis = zoneDateTime.toInstant().toEpochMilli()
                onDateTimeSelected(millis)
            }) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}