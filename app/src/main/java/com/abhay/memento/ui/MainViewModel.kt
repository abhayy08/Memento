package com.abhay.memento.ui

import android.app.Application
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : ViewModelProvider.AndroidVieModel(application) {
    private val _drawerState = MutableStateFlow<DrawerValue>(DrawerValue.Closed)
    val drawerState = _drawerState.asStateFlow() // Expose immutable state flow

    fun toggleDrawer() {
        _drawerState.value = when (_drawerState.value) {
            DrawerValue.Closed -> DrawerValue.Open
            DrawerValue.Open -> DrawerValue.Closed
        }
    }
}