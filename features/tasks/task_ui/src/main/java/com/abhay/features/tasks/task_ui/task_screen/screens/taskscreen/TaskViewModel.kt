package com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.features.tasks.task_ui.task_screen.util.Routes
import com.abhay.features.tasks.task_ui.task_screen.util.UiEvent
import com.abhay.task_data.Todo
import com.abhay.task_data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    val tasks = repository.getTodos()

    private var taskState = mutableStateOf<Todo?>(null)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deleteTodo: Todo? = null

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
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }

            is TaskEvents.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK + "?todoId=${event.todo.id}"))
            }

            is TaskEvents.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deleteTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.showSnackBar(
                            message = "Task Deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TaskEvents.OnUndoDeleteClick -> {
                deleteTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }

            is TaskEvents.OpenOrCreateTask -> {
                if (event.taskId != null) {
                    viewModelScope.launch {
                        repository.getTodoById(event.taskId)?.let { task ->
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
                    repository.insertTodo(
                        Todo(
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