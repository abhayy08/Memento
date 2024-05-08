package com.abhay.features.note.note_ui.notes

sealed class ContainerState {
    object Fab : ContainerState()
    object Fullscreen : ContainerState()
}