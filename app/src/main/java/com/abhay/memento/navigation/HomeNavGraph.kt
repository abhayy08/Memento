package com.abhay.memento.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abhay.common.di.AppModule.provideDrawerStateManager
import com.abhay.features.note.note_ui.notes.NotesUiScreen
import com.abhay.features.note.note_ui.notes.NotesViewModel
import com.abhay.features.tasks.task_ui.task_screen.screens.taskscreen.TaskScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
) {

    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) = {
         fadeIn(animationSpec = tween(durationMillis = 300)) +
                 slideInHorizontally(animationSpec = tween(easing = FastOutSlowInEasing)) { it / 2 }
    }
    val exitTransition:(AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) = {
        slideOutHorizontally(animationSpec = tween(easing = FastOutSlowInEasing)) { -it / 2 } +
                 fadeOut(animationSpec = tween(durationMillis = 300))
    }
    NavHost(
        navController = navController,
        startDestination = AppScreen.Notes.route
    ) {
        composable(
            route = AppScreen.Notes.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            val viewModel: NotesViewModel = hiltViewModel()
            NotesUiScreen(
                viewModel = viewModel,
                navController = navController,
                drawerStateManager = provideDrawerStateManager()
            )
        }
//        composable(
//            route = AppScreen.AddEditNotes.route + "?noteId={noteId}",
//            arguments = listOf(navArgument(
//                name = "noteId"
//            ) {
//                type = NavType.IntType
//                defaultValue = -1
//            })
//        ) {
//            val viewModel: AddEditNotesViewModel = hiltViewModel()
//
//            AddEditNotesScreen(
//                navController = navController,
//                titleState = viewModel.titleState.value,
//                contentState = viewModel.contentState.value,
//                eventFlow = viewModel.eventFlow,
//                onEvent = viewModel::onEvent,
//                isNote = viewModel.isNote
//            )
//        }

        composable(
            route = AppScreen.Tasks.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            TaskScreen(
                drawerStateManager = provideDrawerStateManager()
            )
        }
    }
}