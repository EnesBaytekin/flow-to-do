package com.example.flowtodo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowtodo.AppDatabase
import com.example.flowtodo.DialogAddToDo
import com.example.flowtodo.DialogDeletion
import com.example.flowtodo.DialogEditToDo
import com.example.flowtodo.FlowToDo
import com.example.flowtodo.Task
import com.example.flowtodo.TaskDao
import com.example.flowtodo.ToDoItem
import com.example.flowtodo.ui.theme.FlowToDoTheme
import com.example.flowtodo.R
import kotlinx.coroutines.launch
import java.util.stream.IntStream.range

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewToDoScreen() {
    FlowToDoTheme(true) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            FlowToDo(Modifier.padding(innerPadding))
        }
    }
}

suspend fun loadTasks(taskDao: TaskDao): List<List<Task>> {
    val allTasks = taskDao.getAllTasks()
    val tasks = mutableListOf<List<Task>>()
    for (i in range(0, 7)) {
        tasks.add(
            allTasks
            .filter { task -> task.fromWeekday == i }
            .sortedBy { it.fromHour*60+it.fromMinute }
        )
    }
    return tasks
}

@Composable
fun ToDoScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskDao = db.taskDao()
    val coroutineScope = rememberCoroutineScope()

    var tasks by rememberSaveable { mutableStateOf<List<List<Task>>>(emptyList()) }

    var showAddToDoDialog by rememberSaveable { mutableStateOf(false) }

    var showDeletionPopup by rememberSaveable { mutableStateOf(false) }
    var deletionPopupId by rememberSaveable { mutableStateOf(0) }

    var showEditToDoDialog by rememberSaveable { mutableStateOf(false) }
    var editPopupId by rememberSaveable { mutableStateOf(0) }
    var editPopupDay by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        tasks = loadTasks(taskDao)
    }

    if (showDeletionPopup) {
        DialogDeletion(
            onCancel = { showDeletionPopup = false },
            onConfirm = {
                showDeletionPopup = false
                coroutineScope.launch {
                    taskDao.deleteTaskById(deletionPopupId)
                    tasks = loadTasks(taskDao)
                }
            }
        )
    }
    if (showAddToDoDialog) {
        DialogAddToDo(
            onDismissRequest = { showAddToDoDialog = false },
            onConfirm = { task: Task ->
                showAddToDoDialog = false
                coroutineScope.launch {
                    taskDao.insertTask(task)
                    tasks = loadTasks(taskDao)
                }
            }
        )
    }
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val weekdayNames = listOf(
                stringResource(id=R.string.monday),
                stringResource(id=R.string.tuesday),
                stringResource(id=R.string.wednesday),
                stringResource(id=R.string.thursday),
                stringResource(id=R.string.friday),
                stringResource(id=R.string.saturday),
                stringResource(id=R.string.sunday),
            )
            LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                for ((weekday, dailyTasks) in tasks.withIndex()) {
                    if (dailyTasks.isEmpty()) { continue }
                    item {
                        Text(
                            text = weekdayNames[weekday]
                        )
                    }
                    items(dailyTasks, key = { it.id }) { task ->
                        ToDoItem(
                            task = task,
                            onCheckedChange = {
                                coroutineScope.launch {
                                    taskDao.toggleTaskCompletion(task.id)
                                    tasks = loadTasks(taskDao)
                                }
                            },
                            openDeletionPopup = {
                                showDeletionPopup = true
                                deletionPopupId = task.id
                            },
                            openEditToDoDialog = {
                                showEditToDoDialog = true
                                editPopupId = task.id
                                editPopupDay = weekday
                            }
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }
            }
            ButtonAddToDo(
                onClick = { showAddToDoDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd))
        }
    }
}

@Composable
fun ButtonAddToDo(onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(32.dp),
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .size(160.dp, 40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.add_to_do),
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_to_do),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
