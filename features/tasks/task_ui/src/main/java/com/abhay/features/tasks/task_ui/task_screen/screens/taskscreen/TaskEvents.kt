package com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen

import com.abhay.task_data.Todo

sealed class TaskEvents {

    data class OpenOrCreateTask(val taskId: Int?) : TaskEvents()
    data object SaveTask : TaskEvents()

    data class onTitleChange(val title: String) : TaskEvents()
    data class onDescriptionChange(val description: String) : TaskEvents()

    data class OnDeleteTodoClick(val todo: Todo) : TaskEvents()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean) : TaskEvents()
    data object OnUndoDeleteClick : TaskEvents()
    data class OnTodoClick(val todo: Todo) : TaskEvents()
    data object OnAddTodoClick : TaskEvents()

}