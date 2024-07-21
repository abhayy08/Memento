package com.abhay.features.tasks.task_ui.task_screen.screens.add_edit_tasks.something

sealed class ContainerState {
    object Fab : ContainerState()
    object Fullscreen : ContainerState()
}