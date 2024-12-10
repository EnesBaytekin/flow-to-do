package com.example.flowtodo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.flowtodo.AppDatabase
import com.example.flowtodo.FlowToDo
import com.example.flowtodo.Task
import com.example.flowtodo.ui.theme.FlowToDoTheme
import kotlinx.coroutines.launch

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewToDoScreen() {
    FlowToDoTheme(true) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            FlowToDo(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun ToDoScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskDao = db.taskDao()
    val coroutineScope = rememberCoroutineScope()

    var tasks by rememberSaveable { mutableStateOf<List<Task>>(emptyList()) }

    var showAddToDoDialog by rememberSaveable { mutableStateOf(false) }

    var showDeletionPopup by rememberSaveable { mutableStateOf(false) }
    var deletionPopupId by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
    }

    if (showDeletionPopup) {
        DialogDeletion(
            onCancel = { showDeletionPopup = false },
            onConfirm = {
                showDeletionPopup = false
                coroutineScope.launch {
                    taskDao.deleteTaskById(deletionPopupId)
                    tasks = taskDao.getAllTasks()
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (showAddToDoDialog) {
            DialogAddToDo(
                onDismissRequest = { showAddToDoDialog = false },
                onConfirm = { task: Task ->
                    showAddToDoDialog = false
                    coroutineScope.launch {
                        taskDao.insertTask(task)
                        tasks = taskDao.getAllTasks()
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 40.dp)
            ) {
                items(tasks) { task ->
                    ToDoItem(
                        task = task,
                        onCheckedChange = {
                            coroutineScope.launch {
                                taskDao.toggleTaskCompletion(task.id)
                                tasks = taskDao.getAllTasks()
                            }
                        },
                        openDeletionPopup = {
                            showDeletionPopup = true
                            deletionPopupId = task.id
                        }
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

@Composable
fun DialogAddToDo(onDismissRequest: () -> Unit = {}, onConfirm: (Task) -> Unit = {}) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
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
                            .padding(top = 16.dp)
                            .height(150.dp)
                    )
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
                                onConfirm(Task(title = title, description = description))
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
                    text = "Add To-Do",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add ToDo",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ToDoItem(task: Task, onCheckedChange: () -> Unit = {}, openDeletionPopup: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .wrapContentHeight(unbounded = true)
                        .heightIn(50.dp)
                        .padding(end = 40.dp)
                ) {
                    Text(
                        text = task.title,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    if (task.description != "") {
                        Text(
                            text = task.description,
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                        )
                    }
                }
                IconButton(
                    onClick = openDeletionPopup,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.Delete, "delete task")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLazyColumn() {
    var tasks by rememberSaveable { mutableStateOf<List<Task>>(listOf(
        Task(
            title = "some title",
            description = "some description"
        ),
        Task(
            title = "some other title",
            description = "some different description, some different description some different description some different description some different description some different description some different description some different description some different description some different description some different description some different description"
        ),
        Task(
            title = "other title",
            description = ""
        )
    )) }
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 40.dp)
    ) {
        items(tasks) { task ->
            ToDoItem(
                task = task,
                onCheckedChange = {},
                openDeletionPopup = {}
            )
        }
    }
}
