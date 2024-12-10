package com.example.flowtodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToDoItem(
    task: Task,
    onCheckedChange: () -> Unit = {},
    openDeletionPopup: () -> Unit = {},
    openEditToDoDialog: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable(onClick = openEditToDoDialog)
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
                        .padding(end = 44.dp)
                ) {
                    Text(
                        text = task.title,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Justify
                    )
                    if (task.description != "") {
                        Text(
                            text = task.description,
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Justify,
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
