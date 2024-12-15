package com.example.flowtodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewWeekdayPicker() {
    WeekdayPicker(
        startDay = 0,
        endDay = 2,
        onStartDayChange = { },
        onEndDayChange = { }
    )
}

@Composable
fun WeekdayPicker(
    startDay: Int,
    endDay: Int,
    onStartDayChange: (Int) -> Unit,
    onEndDayChange: (Int) -> Unit
) {
    val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        weekdays.forEachIndexed { index, day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
            ) {
                IconButton(onClick = {
                    onStartDayChange(index)
                }) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Decrease",
                        tint = if (index == startDay)   MaterialTheme.colorScheme.outline
                               else                     MaterialTheme.colorScheme.outlineVariant
                    )
                }
                Text(
                    text = day
                )
                IconButton(onClick = {
                    onEndDayChange(index)
                }) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Increase",
                        tint = if (index == endDay) MaterialTheme.colorScheme.outline
                               else                 MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}
