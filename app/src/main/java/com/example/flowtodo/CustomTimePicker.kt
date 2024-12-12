package com.example.flowtodo

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomTimePicker(
    currentHour: Int,
    currentMinute: Int,
    onDismissRequest: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {
    val context = LocalContext.current
    val hour = currentHour
    val minute = currentMinute

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            onTimeSelected(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true
    )

    timePickerDialog.setOnCancelListener {
        onDismissRequest()
    }

    timePickerDialog.show()
}
