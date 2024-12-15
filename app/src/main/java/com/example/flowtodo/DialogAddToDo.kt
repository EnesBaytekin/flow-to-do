package com.example.flowtodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@Composable
fun DialogAddToDo(onDismissRequest: () -> Unit = {}, onConfirm: (Task) -> Unit = {}) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    var fromHour by rememberSaveable { mutableIntStateOf((currentHour+((currentMinute/30+1)*30)/60+0.5).toInt()%24) }
    var fromMinute by rememberSaveable { mutableIntStateOf(((currentMinute/30+1)*30)%60) }
    var fromWeekday by rememberSaveable { mutableIntStateOf(0) }
    var toHour by rememberSaveable { mutableIntStateOf((currentHour+((currentMinute/30+1)*30)/60+1.5).toInt()%24) }
    var toMinute by rememberSaveable { mutableIntStateOf(((currentMinute/30+1)*30)%60) }
    var toWeekday by rememberSaveable { mutableIntStateOf(0) }

    var showFromTimePicker by remember { mutableStateOf(false) }
    if (showFromTimePicker) {
        CustomTimePicker(
            fromHour,
            fromMinute,
            onDismissRequest = {
                showFromTimePicker = false
            },
            onTimeSelected = { hour: Int, minute: Int ->
                showFromTimePicker = false
                fromHour = hour
                fromMinute = minute
            }
        )
    }
    var showToTimePicker by remember { mutableStateOf(false) }
    if (showToTimePicker) {
        CustomTimePicker(
            toHour,
            toMinute,
            onDismissRequest = {
                showToTimePicker = false
            },
            onTimeSelected = { hour: Int, minute: Int ->
                showToTimePicker = false
                toHour = hour
                toMinute = minute
            }
        )
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = "New To-Do",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Column {
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                singleLine = true,
                                label = {
                                    Text("Title")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            )
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = {
                                    Text("Description")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }
                        WeekdayPicker(
                            startDay = fromWeekday,
                            endDay = toWeekday,
                            onStartDayChange = { newStartDay -> fromWeekday = newStartDay },
                            onEndDayChange = { newEndDay -> toWeekday = newEndDay }
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TimePickerButton(
                                text = "From",
                                hour = fromHour,
                                minute = fromMinute,
                                onClick = {
                                    showFromTimePicker = true
                                }
                            )
                            TimePickerButton(
                                text = "To",
                                hour = toHour,
                                minute = toMinute,
                                onClick = {
                                    showToTimePicker = true
                                }
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDismissRequest() },
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (title != "") {
                                onConfirm(Task(
                                    title = title,
                                    description = description,
                                    fromHour = fromHour,
                                    fromMinute = fromMinute,
                                    fromWeekday = fromWeekday,
                                    toHour = toHour,
                                    toMinute = toMinute,
                                    toWeekday = toWeekday
                                ))
                                title = ""
                                description = ""
                            }
                        },
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDialogAddToDo() {
    DialogAddToDo()
}
