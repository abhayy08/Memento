package com.abhay.features.note.note_ui.navigation

sealed class NotesScreens(
    val route: String
) {
    data object NotesMainScreen : NotesScreens(route = "NOTES")
    data object AddEditScreen : NotesScreens(route = "ADDEDITNOTES")
}