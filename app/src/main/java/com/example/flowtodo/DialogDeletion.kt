package com.example.flowtodo

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DialogDeletion(onCancel: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        text = {
            Text("Do you really want to delete this To-Do?")
        },
        onDismissRequest = onCancel,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun PreviewDeletion() {
    DialogDeletion({}, {})
}
