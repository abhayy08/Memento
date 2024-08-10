package com.abhay.features.note.note_ui.notes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.abhay.features.note.note_ui.addEdtiNoteScreen.AddEditNoteScreen

@Composable
fun NotesFABContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerState: ContainerState,
    onBackClick: (ContainerState) -> Unit,
    isExpanded: Boolean
) {

    val transition = updateTransition(targetState = containerState, label = "")

    val cornerRadius by transition.animateDp(label = "", transitionSpec = {
        tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        )
    }) { state ->
        when (state) {
            ContainerState.Fab -> 16.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }
    val backgroundColor by transition.animateColor(label = "", transitionSpec = {
        tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        )
    }) { state ->
        when (state) {
            ContainerState.Fab -> MaterialTheme.colorScheme.primaryContainer
            ContainerState.Fullscreen -> MaterialTheme.colorScheme.background
        }

    }

    val elevation by transition.animateDp(label = "", transitionSpec = {
        tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        )
    }) { state ->
        when (state) {
            ContainerState.Fab -> 16.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }

    val padding by transition.animateDp(label = "", transitionSpec = {
        tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        )
    }) { state ->
        when (state) {
            ContainerState.Fab -> 24.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }

    transition.AnimatedContent(
        modifier = modifier
            .padding(end = padding, bottom = padding)
            .shadow(
                elevation = elevation, shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind { drawRoundRect(backgroundColor) }) { state ->
        when (state) {
            ContainerState.Fab -> {
                ExtendedFloatingActionButton(text = { Text(text = "Save Note") }, icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add, contentDescription = "Add Note"
                    )
                }, onClick = onClick, expanded = isExpanded
                )
            }

            ContainerState.Fullscreen -> {
                AddEditNoteScreen(onBackClick = { onBackClick(ContainerState.Fab) })
            }
        }
    }
}