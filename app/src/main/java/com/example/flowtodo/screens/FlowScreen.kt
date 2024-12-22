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
import com.example.flowtodo.Task

@Composable
fun FlowScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskDao = db.taskDao()
    val coroutineScope = rememberCoroutineScope()

    var tasks by rememberSaveable { mutableStateOf<List<List<Task>>>(emptyList()) }

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
                .padding(horizontal = 15.dp, vertical = 1.dp)
        ) {
            WeeklyTable(tasks)
        }
    }
}