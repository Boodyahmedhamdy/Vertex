package com.iti.vertex.settings.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import com.iti.vertex.settings.vm.SettingsViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {

    val windSpeedState = viewModel.windSpeedUnitState.collectAsStateWithLifecycle()


    SettingsScreenContent(
        windSpeedUnitState = windSpeedState.value,
        onWindSpeedUnitChanged = { viewModel.setWindSpeedUnit(it) },
        modifier = modifier,
    )


}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    windSpeedUnitState: WindSpeedUnit,
    onWindSpeedUnitChanged: (WindSpeedUnit) -> Unit
) {
    Column(
        modifier = modifier
    ){
        // wind speed card
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            // title
            Text(
                text = "Wind Speed Unit",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            // options
            FlowRow(maxItemsInEachRow = 2) {
                WindSpeedUnit.entries.forEach { windSpeedUnit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = windSpeedUnitState == windSpeedUnit,
                            onClick = { onWindSpeedUnitChanged(windSpeedUnit) }
                        )
                        Text(text = stringResource(windSpeedUnit.displayName))
                    }
                }
            }
        }

        // temperature unit card


    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    maxItemsInEachRow: Int = 2,
    options: List<Any>,
    selected: () -> Boolean,
    onClick: (Any) -> Unit,
    @StringRes displayName: Int
) {
    Card(
        modifier = modifier
    ) {
        // title
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )

        // options
        FlowRow(maxItemsInEachRow = maxItemsInEachRow) {
            options.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RadioButton(
                        selected = selected(),
                        onClick = {
                            onClick(it)
                        }
                    )
                    Text(text = stringResource(displayName))
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {

}