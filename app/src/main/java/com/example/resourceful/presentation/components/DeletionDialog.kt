package com.example.resourceful.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.resourceful.util.AlertMessages

@Composable
fun DeletionDialog(
    description: String,
    onDismiss:() -> Unit,
    onDelete:() -> Unit
) {
    AlertDialog(
        icon = { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Example Icon") },
        title = { Text(text = AlertMessages.deletionTitle) },
        text = { Text(text = description) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onDelete()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Dismiss")
            }
        }
    )
}