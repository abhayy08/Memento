package com.abhay.features.tasks.task_ui.task_screen.screens.add_edit_task

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen.TaskEvents
import com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen.TaskViewModel
import com.abhay.features.tasks.task_ui.task_screen.util.UiEvent
import kotlinx.coroutines.delay

@Composable
fun AddEditTaskScreen(
    onBackClick: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {

    var taskTitle = viewModel.taskTitle.value
    var taskDescription = viewModel.taskDescription.value

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.showSnackBar -> {
                    snackbarHostState.showSnackbar(
                        event.message, actionLabel = event.action, duration = SnackbarDuration.Short
                    )
                }

                else -> Unit
            }
        }
    }

    BackHandler {
        viewModel.onEvent(TaskEvents.SaveTask)
        onBackClick()
    }

    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(24.dp)
            ) {
                TextField(
                    value = taskTitle,
                    onValueChange = {
                        viewModel.onEvent(TaskEvents.onTitleChange(it))
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                TextField(
                    value = taskDescription,
                    onValueChange = {
                        viewModel.onEvent(TaskEvents.onDescriptionChange(it))
                    }
                )
                IconButton(onClick = {
                    viewModel.onEvent(TaskEvents.SaveTask)
                    onBackClick()
                }) {
                    Icon(imageVector = Icons.Rounded.Save, contentDescription = "Save")
                }
            }
        }
    }

}

@Composable
fun AddEditTaskFab(
    onClick: () -> Unit,
    isDeleteAvailable: Boolean,
    onDeleteClick: () -> Unit
) {
    var animationDelay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.End
    ) {
        if (isDeleteAvailable) {
            LaunchedEffect(isDeleteAvailable) {
                delay(1000) // Delay for a bit
                animationDelay = true

            }
            AnimatedVisibility(
                visible = animationDelay,
                enter = slideInVertically { it + 16 },
                exit = slideOutVertically { -it + 16 }
            ) {
                SmallFloatingActionButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete Note")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        ExtendedFloatingActionButton(
            text = { Text(text = "Save") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note"
                )
            },
            onClick = onClick
        )

    }
}