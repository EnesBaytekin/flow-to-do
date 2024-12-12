package com.example.flowtodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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


@Composable
fun DialogEditToDo(
    task: Task,
    onDismissRequest: () -> Unit = { },
    onConfirm: (Task) -> Unit = { },
    initialFromHour: Int = 0,
    initialFromMinute: Int = 0,
    initialFromWeekday: Int = 0,
    initialToHour: Int = 0,
    initialToMinute: Int = 0,
    initialToWeekday: Int = 0
) {
    var title by rememberSaveable { mutableStateOf(task.title) }
    var description by rememberSaveable { mutableStateOf(task.description) }

    var fromHour by rememberSaveable { mutableIntStateOf(initialFromHour) }
    var fromMinute by rememberSaveable { mutableIntStateOf(initialFromMinute) }
    var fromWeekday by rememberSaveable { mutableIntStateOf(initialFromWeekday) }
    var toHour by rememberSaveable { mutableIntStateOf(initialToHour) }
    var toMinute by rememberSaveable { mutableIntStateOf(initialToMinute) }
    var toWeekday by rememberSaveable { mutableIntStateOf(initialToWeekday) }

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

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
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
                    text = "Edit To-Do",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .wrapContentHeight()
                        ) {
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
                                    .padding(bottom = 16.dp)
                                    .height(150.dp)
                            )
                        }
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
                                onConfirm(
                                    Task(
                                        id = task.id,
                                        title = title,
                                        description = description,
                                        task.isCompleted,
                                        fromHour = fromHour,
                                        fromMinute = fromMinute,
                                        fromWeekday = fromWeekday,
                                        toHour = toHour,
                                        toMinute = toMinute,
                                        toWeekday = toWeekday
                                    )
                                )
                                title = ""
                                description = ""
                            }
                        },
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewEditToDo() {
    DialogEditToDo(Task(
        title = "",
        description = "",
        fromHour = 0,
        fromMinute = 0,
        fromWeekday = 0,
        toHour = 0,
        toMinute = 0,
        toWeekday = 0
    ))
}
