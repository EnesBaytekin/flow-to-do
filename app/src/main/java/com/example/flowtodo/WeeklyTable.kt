import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
fun WeeklyTable(
    tasks: List<List<Task>>,
    openEditToDoDialog: (Int, Int) -> Unit
) {
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
    var taskUnitWidth by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            Row() {
                Box(
                    modifier = Modifier
                        .width(15.dp)
                ) {
                    for (i in range(0, 24)) {
                        Text(
                            text = "%d".format(i),
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.8f
                            ),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 2.dp)
                                .offset(0.dp, taskUnitHeight * i + 12.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        modifier = Modifier
                            .fillMaxHeight()
                            .onGloballyPositioned { layoutCoordinates ->
                                taskUnitWidth =
                                    with(density) { (layoutCoordinates.size.width / 7.0f).toDp() }
                            }
                    ) {
                        for (columnIndex in range(0, 7)) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
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
                                ) {
                                    Card(
                                        colors = CardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                            contentColor = CardDefaults.cardColors().contentColor,
                                            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
                                            disabledContentColor = CardDefaults.cardColors().disabledContentColor
                                        ),
                                        shape = RoundedCornerShape(
                                            topStart = 8.dp,
                                            topEnd = 8.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 0.dp
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
                                            .height(taskUnitHeight * 24.0f)
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                    ) {
                                    }
                                }
                            }
                        }
                    }
                    for (i in range(1, 25)) {
                        Line(taskUnitHeight * i + 24.dp)
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxSize()
                    ) {
                        if (tasks.isNotEmpty()) {
                            for (columnIndex in range(0, 7)) {
                                tasks[columnIndex].forEach() { task ->
                                    TaskBox(task, taskUnitWidth, taskUnitHeight, openEditToDoDialog)
                                }
                            }
                        }
                    }
                    Line(24.dp)
                }
            }
        }
    }
}

@Composable
fun TaskBox(
    task: Task,
    taskUnitWidth: Dp,
    taskUnitHeight: Dp,
    openEditToDoDialog: (Int, Int) -> Unit
) {
    val customColor = getColorFromText(task.title)
    val cardColor = blendColors(CardDefaults.cardColors().containerColor, customColor, 0.2f).copy(alpha = 0.9f)

    var taskStartHour = task.fromHour+task.fromMinute/60.0f
    var taskFinishHour = task.toHour+task.toMinute/60.0f

    var taskStartDay = task.fromWeekday

    if (task.fromWeekday == task.toWeekday && taskFinishHour-taskStartHour < 0) {
        taskStartDay += 7
    }

    while (taskStartDay != task.toWeekday) {
        taskFinishHour = 24.0f

        val firstDay = (taskStartDay%7) == task.fromWeekday
        val x = taskUnitWidth*(taskStartDay%7)
        val y = taskUnitHeight*taskStartHour
        val height = taskUnitHeight*(taskFinishHour-taskStartHour)
        TaskCard(
            task.title,
            x, y,
            taskUnitWidth,
            height,
            cardColor,
            firstDay,
            false,
            openEditToDoDialog = {
                openEditToDoDialog(task.id, task.fromWeekday)
            }
        )

        taskStartDay = (taskStartDay+1)%7
        taskStartHour = 0.0f
    }

    taskFinishHour = task.toHour+task.toMinute/60.0f

    val firstDay = taskStartDay == task.fromWeekday
    val x = taskUnitWidth*taskStartDay
    val y = taskUnitHeight*taskStartHour
    val height = taskUnitHeight*(taskFinishHour-taskStartHour)
    TaskCard(
        task.title,
        x, y,
        taskUnitWidth,
        height,
        cardColor,
        firstDay,
        true,
        openEditToDoDialog = {
            openEditToDoDialog(task.id, task.fromWeekday)
        }
    )
}

@Composable
fun TaskCard(
    title: String,
    offsetX: Dp,
    offsetY: Dp,
    width: Dp,
    height: Dp,
    cardColor: Color,
    roundTopCorners: Boolean,
    roundBottomCorners: Boolean,
    openEditToDoDialog: () -> Unit
) {
    val topCorners = if (roundTopCorners) 12.dp else 0.dp
    val bottomCorners = if (roundBottomCorners) 12.dp else 0.dp
    Card(
        colors = CardColors(
            containerColor = cardColor,
            contentColor = CardDefaults.cardColors().contentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor
        ),
        shape = RoundedCornerShape(
            topStart = topCorners,
            topEnd = topCorners,
            bottomStart = bottomCorners,
            bottomEnd = bottomCorners
        ),
        modifier = Modifier
            .offset(offsetX, offsetY)
            .size(width, height)
            .padding(end = 1.dp)
            .clickable { openEditToDoDialog() }
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .padding(top = 2.dp, bottom = if (roundBottomCorners) 2.dp else 0.dp)
        )
    }
}

@Composable
fun Line(offsetY: Dp) {
    Box(
        modifier = Modifier
            .offset(0.dp, offsetY)
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {}
}
