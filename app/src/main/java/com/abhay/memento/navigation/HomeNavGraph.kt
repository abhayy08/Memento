package com.abhay.memento.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.abhay.features.note.note_ui.add_edit_note_screen.AddEditNotesScreen
import com.abhay.features.note.note_ui.add_edit_note_screen.AddEditNotesViewModel
import com.abhay.features.note.note_ui.notes.NotesUiScreen
import com.abhay.features.note.note_ui.notes.NotesViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Notes.route
    ) {
        composable(route = AppScreen.Notes.route) {
            val viewModel: NotesViewModel = hiltViewModel()
            NotesUiScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            route = AppScreen.AddEditNotes.route + "?noteId={noteId}",
            arguments = listOf(navArgument(
                name = "noteId"
            ) {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            val viewModel: AddEditNotesViewModel = hiltViewModel()
            AddEditNotesScreen(
                navController = navController,
                titleState = viewModel.titleState.value,
                contentState = viewModel.contentState.value,
                eventFlow = viewModel.eventFlow,
                onEvent = viewModel::onEvent,
                isNote = viewModel.isNote
            )
        }
    }
}