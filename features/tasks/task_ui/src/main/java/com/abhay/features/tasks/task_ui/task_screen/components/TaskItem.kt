package com.abhay.features.tasks.task_ui.task_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen.TaskEvents
import com.abhay.task_data.Task

@Composable
fun TaskItem(
    task: Task,
    onEvent: (TaskEvents) -> Unit,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val textDecoration: TextDecoration = if (task.isDone) {
        TextDecoration.LineThrough
    } else {
        TextDecoration.None
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularCheckbox(modifier = Modifier.clip(RoundedCornerShape(100.dp)),
            checked = task.isDone,
            onCheckedChange = {
                onEvent(TaskEvents.OnDoneChange(task, it))
            })
        Divider(
            modifier = Modifier.width(36.dp),
            thickness = 2.dp,
        )
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                onItemClick()
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                        .weight(5f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = task.title + " ",
                        fontSize = 16.sp,
                        textDecoration = textDecoration,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (task.description!!.isNotBlank()) {
                        Text(
                            text = task.description!! + " ",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textDecoration = textDecoration,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Divider(
                        Modifier
                            .height(30.dp)
                            .width(1.dp)
                    )
                    IconButton(onClick = {
                        onDeleteClick()
                    }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun Asd() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularCheckbox(modifier = Modifier.clip(RoundedCornerShape(100.dp)),
            checked = false,
            onCheckedChange = {})
        Divider(modifier = Modifier.width(36.dp))
        Card(
            modifier = Modifier.fillMaxWidth()

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Do Laundry")
                    Text(
                        text = "Kapde Dhole Jaake Bdsk", color = Color.Gray
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete Task")
                }
                Row {
                    Divider()
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun CircularCheckbox(
    modifier: Modifier = Modifier, checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.6f
                ), shape = CircleShape
            )
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary), RoundedCornerShape(16.dp)
            )
            .clickable { onCheckedChange(!checked) }, contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
