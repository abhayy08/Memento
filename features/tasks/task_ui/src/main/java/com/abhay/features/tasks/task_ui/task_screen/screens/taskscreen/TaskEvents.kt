package com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen

import com.abhay.task_data.Task

sealed class TaskEvents {

    data class OpenOrCreateTask(val taskId: Int?) : TaskEvents()
    data object SaveTask : TaskEvents()

    data class onTitleChange(val title: String) : TaskEvents()
    data class onDescriptionChange(val description: String) : TaskEvents()

    data object ClearAllCompletedTasks: TaskEvents()

    data class OnDeleteTodoClick(val task: Task) : TaskEvents()
    data class OnDoneChange(val task: Task, val isDone: Boolean) : TaskEvents()
    data object OnUndoDeleteClick : TaskEvents()
    data class OnTodoClick(val task: Task) : TaskEvents()
    data object OnAddTodoClick : TaskEvents()

}