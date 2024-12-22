package com.example.flowtodo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val weekdays = listOf(
        stringResource(R.string.mon),
        stringResource(R.string.tue),
        stringResource(R.string.wed),
        stringResource(R.string.thu),
        stringResource(R.string.fri),
        stringResource(R.string.sat),
        stringResource(R.string.sun)
    )

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
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (index == startDay) {
                        Text(
                            text = stringResource(R.string.from),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 24.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            onStartDayChange(index)
                        }
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.from),
                            tint = if (index == startDay) MaterialTheme.colorScheme.outline
                            else CardDefaults.cardColors().containerColor
                        )
                    }
                }
                Text(
                    text = day,
                    fontWeight = FontWeight.ExtraBold
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            onEndDayChange(index)
                        }
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = stringResource(R.string.to),
                            tint = if (index == endDay) MaterialTheme.colorScheme.outline
                            else CardDefaults.cardColors().containerColor
                        )
                    }
                    if (index == endDay) {
                        Text(
                            text = stringResource(R.string.to),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                        )
                    }
                }
            }
        }
    }
}
