import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowtodo.R
import com.example.flowtodo.Task
import com.example.flowtodo.blendColors
import com.example.flowtodo.getColorFromText
import java.util.stream.IntStream.range

@Composable
fun WeeklyTable(tasks: List<List<Task>>) {
    val headers = listOf(
        stringResource(R.string.monday),
        stringResource(R.string.tuesday),
        stringResource(R.string.wednesday),
        stringResource(R.string.thursday),
        stringResource(R.string.friday),
        stringResource(R.string.saturday),
        stringResource(R.string.sunday),
    )

    val taskUnitHeight = 40.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                for (columnIndex in range(0, 7)) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Card(
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = CardDefaults.cardColors().contentColor,
                                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
                                disabledContentColor = CardDefaults.cardColors().disabledContentColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Card(
                                colors = CardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    contentColor = CardDefaults.cardColors().contentColor,
                                    disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
                                    disabledContentColor = CardDefaults.cardColors().disabledContentColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                            ) {
                                Text(
                                    text = headers[columnIndex],
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 8.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(taskUnitHeight * 24)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                            ) {
                                for (i in range(0, 25)) {
                                    Box(
                                        modifier = Modifier
                                            .offset(0.dp, taskUnitHeight * i)
                                            .height(1.dp)
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background)
                                    ) {

                                    }
                                }
                                if (tasks.isNotEmpty()) {
                                    tasks[columnIndex].forEach() { task ->
                                        TaskBox(task, taskUnitHeight)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskBox(
    task: Task,
    taskUnitHeight: Dp
) {
    val taskStart = (task.fromHour+task.fromMinute/60.0f)
    val taskFinish = (task.toHour+task.toMinute/60.0f)
    val taskDuration = taskFinish-taskStart

    val taskOffsetY = taskUnitHeight*taskStart
    val taskHeight = taskUnitHeight*taskDuration

    val customColor = getColorFromText(task.title)
    val cardColor = blendColors(CardDefaults.cardColors().containerColor, customColor, 0.2f).copy(alpha = 0.9f)
    Card(
        colors = CardColors(
            containerColor = cardColor,
            contentColor = CardDefaults.cardColors().contentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor
        ),
        modifier = Modifier
            .offset(0.dp, taskOffsetY)
            .height(taskHeight)
            .fillMaxWidth()
    ) {
        Text(
            text = task.title,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            modifier = Modifier
                .padding(2.dp)
        )
    }
}
