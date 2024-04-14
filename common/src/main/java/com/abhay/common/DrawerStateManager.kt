package com.abhay.common

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue

object DrawerStateManager {
    val drawerState = DrawerState(initialValue = DrawerValue.Closed)

    suspend fun openDrawer() {
        drawerState.open()
    }

    suspend fun closeDrawer() {
        drawerState.close()
    }

    suspend fun toggleDrawer() {
        if (drawerState.isClosed) {
            openDrawer()
        } else {
            closeDrawer()
        }
    }
}