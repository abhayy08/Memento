package com.abhay.features.tasks.task_ui.task_screen.components.tryingcomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskItem2(
    modifier: Modifier = Modifier,
    task: Task,
    onItemClick: () -> Unit = {},
    onTaskCompletion : (Boolean) -> Unit = {},
    onSubTaskCompletion: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable{
                onItemClick()
            },
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isDone, onCheckedChange = {
                onTaskCompletion(it)
            })
            Column(
                horizontalAlignment = Alignment.Start,
            ){
                Text(text = task.title)
                Text(text = task.description ?: "", fontSize = 11.sp, modifier = Modifier.alpha(0.7f))
            }
        }

        if(task.subList.isNotEmpty()){
            task.subList.forEach { subTask ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .scale(0.9f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = subTask.isDone, onCheckedChange = {
                        onSubTaskCompletion(it)
                    })
                    Text(text = subTask.title)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Asd() {
    TaskItem2(task = taskList[1])
}


val taskList = listOf(
    Task(
        title = "Finish Project Report",
        description = "Complete the final report for the Android project",
        subList = listOf(
            SubTask(title = "Write introduction", isDone = false, id = 1),
            SubTask(title = "Complete diagrams", isDone = true, id = 2),
            SubTask(title = "Review and edit", isDone = false, id = 3)
        ),
        isDone = false,
        id = 1
    ),
    Task(
        title = "Grocery Shopping",
        description = "Buy essentials for the week",
        subList = listOf(
            SubTask(title = "Milk", isDone = true, id = 4),
            SubTask(title = "Eggs", isDone = false, id = 5),
            SubTask(title = "Bread", isDone = true, id = 6),
            SubTask(title = "Vegetables", isDone = false, id = 7)
        ),
        isDone = false,
        id = 2
    ),
    Task(
        title = "Workout Session",
        description = "Cardio and strength training",
        subList = listOf(
            SubTask(title = "Warm-up", isDone = true, id = 8),
            SubTask(title = "30 mins running", isDone = false, id = 9),
            SubTask(title = "Strength exercises", isDone = false, id = 10)
        ),
        isDone = false,
        id = 3
    ),
    Task(
        title = "Prepare Presentation",
        description = null,
        subList = listOf(
            SubTask(title = "Create slides", isDone = false, id = 11),
            SubTask(title = "Practice delivery", isDone = false, id = 12)
        ),
        isDone = false,
        id = 4
    ),
    Task(
        title = "Read a Book",
        description = "Start reading 'Clean Code' by Robert C. Martin",
        subList = listOf(
            SubTask(title = "Chapter 1", isDone = false, id = 13),
            SubTask(title = "Chapter 2", isDone = false, id = 14),
            SubTask(title = "Chapter 3", isDone = false, id = 15)
        ),
        isDone = false,
        id = 5
    )
)

