package com.abhay.features.note.note_ui.add_edit_note_screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.abhay.features.note.note_ui.components.TransparentTextField
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditNotesScreen(
    navController: NavHostController,
    titleState :NoteTextFieldState,
    contentState: NoteTextFieldState,
    eventFlow: SharedFlow<AddEditNotesViewModel.UiEvent>,
    onEvent: (AddEditNoteEvent) -> Unit,
    isNote: Boolean
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNotesViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                is AddEditNotesViewModel.UiEvent.DeleteNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    BackHandler {
        onEvent(AddEditNoteEvent.SaveNote)
        navController.navigateUp()
    }


    Scaffold(
        topBar = {
            AddEditTopAppBar(
                navController = navController,
                onDelete = {
                    onEvent(AddEditNoteEvent.DeleteNote)
                },
                onSave = {
                    onEvent(AddEditNoteEvent.SaveNote)
                },
                isDeleteAvailable = isNote
            )
        },
        floatingActionButton = {
            AddEditFab(
                onClick = {
                    onEvent(AddEditNoteEvent.SaveNote)
                }
            )
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
//                .padding(paddingValues)
        ) {
            TransparentTextField(
                modifier = Modifier.fillMaxWidth(),
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    onEvent(AddEditNoteEvent.onTitleChange(it))
                },
                onFocusChange = {
                    onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 25.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                modifier = Modifier.fillMaxSize(),
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    onEvent(AddEditNoteEvent.onNoteChange(it))
                },
                onFocusChange = {
                    onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                singleLine = false,
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 15.sp
                )
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTopAppBar(
    navController: NavHostController,
    isDeleteAvailable: Boolean,
    onDelete: () -> Unit,
    onSave: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            if (isDeleteAvailable) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete Note"
                    )
                }
            }
            IconButton(onClick = onSave) {
                Icon(
                    imageVector = Icons.Rounded.Save,
                    contentDescription = "Delete Note"
                )
            }
        }
    )
}

@Composable
fun AddEditFab(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(imageVector = Icons.Rounded.Check, contentDescription = "Save Note")
    }


}