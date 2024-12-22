package com.example.flowtodo

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DialogDeletion(onCancel: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        text = {
            Text(stringResource(R.string.are_you_sure_delete))
        },
        onDismissRequest = onCancel,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun PreviewDeletion() {
    DialogDeletion({}, {})
}
