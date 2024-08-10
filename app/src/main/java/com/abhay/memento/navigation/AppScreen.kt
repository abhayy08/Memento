package com.abhay.memento.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.NotificationAdd
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Tasks : AppScreen(
        route = "TASKS",
        title = "Tasks",
        icon = Icons.Rounded.AddTask
    )

    data object Notes : AppScreen(
        route = "NOTES",
        title = "Notes",
        icon = Icons.Outlined.EditNote
    )

    data object AddEditNotes : AppScreen(
        route = "ADDEDITNOTES",
        title = "",
        icon = Icons.Outlined.EditNote
    )
}
