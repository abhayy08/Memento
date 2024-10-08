package com.abhay.features.tasks.task_ui.task_screen.util

import androidx.core.app.NotificationCompat.MessagingStyle.Message

sealed class UiEvent {

    data object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class showSnackBar(
        val message: String,
        val action: String? = null
    ) :UiEvent()
}