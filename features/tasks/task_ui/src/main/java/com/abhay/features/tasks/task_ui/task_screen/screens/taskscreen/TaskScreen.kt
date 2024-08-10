@file:JvmName("TaskScreenKt")

package com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhay.common.DrawerStateManager
import com.abhay.features.tasks.task_ui.task_screen.components.TaskItem
import com.abhay.features.tasks.task_ui.task_screen.screens.add_edit_task.ContainerState
import com.abhay.features.tasks.task_ui.task_screen.screens.add_edit_task.TasksFabAddEditContent
import com.abhay.features.tasks.task_ui.task_screen.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    drawerStateManager: DrawerStateManager,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks.collectAsState(initial = emptyList())

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    var containerState by remember {
        mutableStateOf<ContainerState>(ContainerState.Fab)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.showSnackBar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TaskEvents.OnUndoDeleteClick)
                    }
                }

                else -> Unit
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(bottom = 55.dp)
                    )
                },
                topBar = {
                    TasksTopAppbar(
                        scrollBehavior = scrollBehavior,
                        onMenuClick = {
                            scope.launch {
                                drawerStateManager.openDrawer()
                            }
                        },
                        onActionClick = { viewModel.onEvent(TaskEvents.ClearAllCompletedTasks) }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    state = listState,
                ) {
                    items(
                        items = tasks.value.sortedBy { it.isDone },
                        key = { "${it.id ?: 0} - ${it.isDone} - ${it.title}" }
                    ) { task ->
                        TaskItem(
                            task = task,
                            onEvent = viewModel::onEvent,
                            onItemClick = {
                                viewModel.onEvent(TaskEvents.OpenOrCreateTask(task.id))
                                containerState = ContainerState.Fullscreen
                            },
                            onDeleteClick = {
                                viewModel.onEvent(TaskEvents.OnDeleteTodoClick(task))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement()
                                .padding(vertical = 2.dp)
                        )
                    }
                }

            }
            TasksFabAddEditContent(
                onClick = {
                    viewModel.onEvent(TaskEvents.OpenOrCreateTask(null))
                    containerState = ContainerState.Fullscreen
                },
                containerState = containerState,
                onBackClick = { state ->
                    containerState = state
                },
                isExpanded = expandedFab,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksTopAppbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClick: () -> Unit,
    onActionClick: () -> Unit
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Tasks",
                fontSize = 26.sp,
                modifier = Modifier.padding(6.dp),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = { onActionClick() }) {
                Icon(
                    imageVector = Icons.Outlined.ClearAll,
                    contentDescription = "Clear All Completed Tasks"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}