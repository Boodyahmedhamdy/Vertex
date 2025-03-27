package com.iti.vertex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest /*{ showDialog.value = false }*/,
        modifier = modifier
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Important Note", style = MaterialTheme.typography.titleLarge)

                Text(text = "Vertex needs location permissions to work correctly")

                Row {
                    TextButton (
                        onClick = onDismissRequest
                    ) { Text(text = "Dismiss") }

                    TextButton(
                        onClick = onConfirmRequest
                    ) { Text(text = "Allow Permissions") }
                }

            }
        }
    }
}