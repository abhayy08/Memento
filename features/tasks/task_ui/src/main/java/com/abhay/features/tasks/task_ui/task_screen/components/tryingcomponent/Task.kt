package com.abhay.features.tasks.task_ui.task_screen.components.tryingcomponent


data class Task(
    val title: String,
    val description: String?,
    val subList: List<SubTask>,
    val isDone: Boolean,
    val id: Int? = null
)

data class SubTask(
    val title: String,
    val isDone: Boolean = false,
    val id: Int?
)
