package com.example.flowtodo.screens

import WeeklyTable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flowtodo.AppDatabase
import com.example.flowtodo.DialogEditToDo
import com.example.flowtodo.Task
import kotlinx.coroutines.launch

@Composable
fun FlowScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskDao = db.taskDao()
    val coroutineScope = rememberCoroutineScope()

    var tasks by rememberSaveable { mutableStateOf<List<List<Task>>>(emptyList()) }

    var showEditToDoDialog by rememberSaveable { mutableStateOf(false) }
    var editPopupId by rememberSaveable { mutableStateOf(0) }
    var editPopupDay by rememberSaveable { mutableStateOf(0) }

    if (tasks.isNotEmpty()) {
        val taskToEdit = tasks[editPopupDay].find { it.id == editPopupId }
        if (showEditToDoDialog and (taskToEdit != null)) {
            DialogEditToDo(
                task = taskToEdit!!,
                onDismissRequest = { showEditToDoDialog = false },
                onConfirm = { task: Task ->
                    showEditToDoDialog = false
                    coroutineScope.launch {
                        taskDao.updateTask(task)
                        tasks = loadTasks(taskDao)
                    }
                },
                initialFromHour = taskToEdit.fromHour,
                initialFromMinute = taskToEdit.fromMinute,
                initialFromWeekday = taskToEdit.fromWeekday,
                initialToHour = taskToEdit.toHour,
                initialToMinute = taskToEdit.toMinute,
                initialToWeekday = taskToEdit.toWeekday,
            )
        }
    }


    LaunchedEffect(Unit) {
        tasks = loadTasks(taskDao)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 15.dp)
                .padding(vertical = 1.dp)
        ) {
            WeeklyTable(
                tasks = tasks,
                openEditToDoDialog = { id: Int, day: Int ->
                    showEditToDoDialog = true
                    editPopupId = id
                    editPopupDay = day
                }
            )
        }
    }
}