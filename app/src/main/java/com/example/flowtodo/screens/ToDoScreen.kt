package com.example.flowtodo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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

    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }

    var showAddToDoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (showAddToDoDialog) {
            DialogAddToDo(
                onDismissRequest = { showAddToDoDialog = false },
                onConfirm = { name: String ->
                    showAddToDoDialog = false
                    coroutineScope.launch {
                        taskDao.insertTask(Task(title = name))
                        tasks = taskDao.getAllTasks()
                    }
                }
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                items(tasks) { task ->
                    ToDoItem(
                        task = task,
                        onCheckedChange = {
                            coroutineScope.launch {
                                taskDao.toggleTaskCompletion(task.id)
                                tasks = taskDao.getAllTasks()
                            }
                        }
                    )
                }
            }
            ButtonAddToDo(onClick = { showAddToDoDialog = true })
        }
    }
}

@Composable
fun DialogAddToDo(onDismissRequest: () -> Unit = {}, onConfirm: (String) -> Unit = {}) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
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
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        label = {
                            Text("Title")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = {
                            Text("Description")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(200.dp)
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
                        onClick = { onConfirm(name) },
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonAddToDo(onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
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
fun ToDoItem(task: Task, onCheckedChange: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = task.title,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "explanation",
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                    )
                }
            }
        }
    }
}
