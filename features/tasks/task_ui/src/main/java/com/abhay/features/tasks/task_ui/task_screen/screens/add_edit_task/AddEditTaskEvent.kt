package com.abhay.features.tasks.task_ui.task_screen.screens.add_edit_task

sealed class AddEditTaskEvent {
    data class OnTitleChange(val title: String) : AddEditTaskEvent()
    data class OnDescriptionChange(val description: String) : AddEditTaskEvent()
    data object OnSaveTaskClick : AddEditTaskEvent()
}