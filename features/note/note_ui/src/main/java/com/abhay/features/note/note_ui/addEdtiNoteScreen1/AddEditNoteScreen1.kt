package com.abhay.features.note.note_ui.addEdtiNoteScreen1

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhay.features.note.note_ui.components.TransparentTextField
import com.abhay.features.note.note_ui.notes.ContainerState
import com.abhay.features.note.note_ui.notes.NotesEvent
import com.abhay.features.note.note_ui.notes.NotesViewModel
import kotlinx.coroutines.delay

@Composable
fun AddEditNoteScreen1(
    viewModel: NotesViewModel = hiltViewModel(),
    onBackClick: (ContainerState) -> Unit
) {

    BackHandler {
        viewModel.onEvent(NotesEvent.SaveNote)
        onBackClick(ContainerState.Fab)
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddEditFab(
                onClick = {
                    viewModel.onEvent(NotesEvent.SaveNote)
                    onBackClick(ContainerState.Fab)
                },
                isDeleteAvailable = viewModel.isNote, // isNote
                onDeleteClick = {
                    viewModel.onEvent(NotesEvent.DeleteNote)
                    onBackClick(ContainerState.Fab)
                }
            )
        },
        topBar = {
            AddEditTopAppBar(
                onBackClick = {
                    onBackClick(ContainerState.Fab)
                    viewModel.onEvent(NotesEvent.OnBackClick)
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            TransparentTextField(
                modifier = Modifier.fillMaxWidth(),
                text = viewModel.noteTitle.value,
                hint = "Title",
                onValueChange = { viewModel.onEvent(NotesEvent.OnTitleChange(it)) },
                onFocusChange = { viewModel.onEvent(NotesEvent.ChangeTitleFocus(it)) },
                isHintVisible = viewModel.isTitleHintVisible.value,
                singleLine = false,
                maxLines = 3,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            TransparentTextField(
                modifier = Modifier.fillMaxSize(),
                text = viewModel.noteContent.value,
                hint = "Content",
                onValueChange = { viewModel.onEvent(NotesEvent.OnContentChange(it)) },
                onFocusChange = { viewModel.onEvent(NotesEvent.ChangeContentFocus(it)) },
                isHintVisible = viewModel.isContentHintVisible.value,
                singleLine = false,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun AddEditFab(
    onClick: () -> Unit,
    isDeleteAvailable: Boolean,
    onDeleteClick: () -> Unit
) {
    var animationDelay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.End
    ) {
        if (isDeleteAvailable) {
            LaunchedEffect(isDeleteAvailable) {
                delay(500) // Delay for 1 second
                animationDelay = true

            }
            AnimatedVisibility(
                visible = animationDelay,
                enter = slideInVertically { it + 16 },
                exit = slideOutVertically { -it + 16 }
            ) {
                SmallFloatingActionButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete Note")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        ExtendedFloatingActionButton(
            text = { Text(text = "Save") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note"
                )
            },
            onClick = onClick
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTopAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back To Note Screen"
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
//    AddEditNoteScreen1({})
}