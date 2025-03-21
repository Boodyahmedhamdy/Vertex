package com.iti.vertex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PlaceholderScreen(
    title: String,
    onPrimaryButtonClicked: () -> Unit,
    onSecondaryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(16.dp))
        Button(onClick = onPrimaryButtonClicked) {
            Text(text = "Primary Action")
        }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onSecondaryButtonClicked) {
            Text(text = "Secondary Action")
        }

    }

}