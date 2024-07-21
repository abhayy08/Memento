package com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.features.tasks.task_ui.task_screen.util.Routes
import com.abhay.features.tasks.task_ui.task_screen.util.UiEvent
import com.abhay.task_data.Task
import com.abhay.task_data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.getTasks()

    private var taskState = mutableStateOf<Task?>(null)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deleteTask: Task? = null

    private val _taskTitle = mutableStateOf<String>("")
    val taskTitle: State<String> = _taskTitle

    private val _taskDescription = mutableStateOf<String>("")
    val taskDescription: State<String> = _taskDescription

    private val _taskId = mutableStateOf<Int?>(null)


    fun onEvent(event: TaskEvents) {
        when (event) {
            is TaskEvents.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK))
            }

            is TaskEvents.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTask(
                        event.task.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }

            is TaskEvents.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK + "?todoId=${event.task.id}"))
            }

            is TaskEvents.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deleteTask = event.task
                    repository.deleteTask(event.task)
                    sendUiEvent(
                        UiEvent.showSnackBar(
                            message = "Task Deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TaskEvents.OnUndoDeleteClick -> {
                deleteTask?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTask(todo)
                    }
                }
            }

            is TaskEvents.OpenOrCreateTask -> {
                if (event.taskId != null) {
                    viewModelScope.launch {
                        repository.getTaskById(event.taskId)?.let { task ->
                            taskState.value = task
                            _taskTitle.value = task.title
                            _taskDescription.value = task.description ?: ""
                            _taskId.value = task.id
                        }
                    }
                } else {
                    resetTaskStateAndTitle()
                }
            }

            TaskEvents.SaveTask -> {
                viewModelScope.launch {
                    if(_taskTitle.value.isBlank()){
                        sendUiEvent(UiEvent.showSnackBar("Title can't be empty"))
                        return@launch
                    }
                    repository.insertTask(
                        Task(
                            title = _taskTitle.value,
                            description = _taskDescription.value,
                            isDone = false,
                            id = _taskId.value
                        )
                    )
                    resetTaskStateAndTitle()
                }
            }

            is TaskEvents.onDescriptionChange -> {
                _taskDescription.value = event.description
            }
            is TaskEvents.onTitleChange -> {
                _taskTitle.value = event.title
            }

            is TaskEvents.ClearAllCompletedTasks -> {
                viewModelScope.launch {
                    tasks.collect { taskList ->
                        val completedTasks = taskList.filter { it.isDone }
                        completedTasks.forEach { it->
                            repository.deleteTask(it)
                        }
                    }
                }
                sendUiEvent(UiEvent.showSnackBar("Completed tasks cleared"))
            }
        }
    }

    private fun resetTaskStateAndTitle() {
        _taskTitle.value = ""
        _taskDescription.value = ""
        taskState.value = null
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}