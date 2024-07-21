package com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.abhay.common.DrawerStateManager

@Composable
fun TaskScreen(
    drawerStateManager: DrawerStateManager
) {
    MainTaskScreen()
}

@Preview(showBackground = true)
@Composable
private fun TaskPreview() {
    TaskScreen(drawerStateManager = DrawerStateManager)
}