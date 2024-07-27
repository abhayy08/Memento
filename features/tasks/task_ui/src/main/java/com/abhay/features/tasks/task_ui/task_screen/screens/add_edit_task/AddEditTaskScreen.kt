package com.abhay.features.tasks.task_ui.task_screen.screens.add_edit_task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhay.features.tasks.task_ui.task_screen.components.CustomTextField
import com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen.TaskEvents
import com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen.TaskViewModel

@Composable
fun AddEditTaskScreen(
    onBackClick: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
    onEvent: (TaskEvents) -> Unit
) {

    val taskTitle = viewModel.taskTitle.value
    val taskDescription = viewModel.taskDescription.value

    BackHandler {
        viewModel.onEvent(TaskEvents.SaveTask)
        onBackClick()
    }

    Surface {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(8.dp),
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add Task",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Button(
                    onClick = {
                        onEvent(TaskEvents.SaveTask)
                        onBackClick()
                    }, colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(text = "Save", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Task Title",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                )
                CustomTextField(
                    value = taskTitle,
                    onValueChange = { onEvent(TaskEvents.onTitleChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 1,
                    placeholder = "Enter a Task title"
                )
                Spacer(modifier = Modifier.height(32.dp))
//                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Description",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                    value = taskDescription,
                    onValueChange = { onEvent(TaskEvents.onDescriptionChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 10,
                    placeholder = "Enter Description"
                )
            }
        }
    }

}