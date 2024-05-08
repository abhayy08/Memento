package com.abhay.features.note.note_ui.notes

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.abhay.common.DrawerStateManager
import com.abhay.features.note.note_ui.R
import com.abhay.features.note.note_ui.addEdtiNoteScreen1.AddEditNoteScreen1
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesUiScreen(
    viewModel: NotesViewModel,
    navController: NavHostController,
    drawerStateManager: DrawerStateManager
) {
    val state = viewModel.notesState.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var containerState by remember {
        mutableStateOf<ContainerState>(ContainerState.Fab)
    }

    val listState = rememberLazyStaggeredGridState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.onBackground
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
        ) {
            Scaffold(modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
                NoteTopAppBar(scrollBehavior, onMenuClick = {
                    scope.launch {
                        drawerStateManager.openDrawer()
                    }
                }, onActionClick = {
                    Toast.makeText(context, "You tapped your Profile", Toast.LENGTH_SHORT).show()
                })
            }) { paddingvalues ->
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Adaptive(156.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp, vertical = paddingvalues.calculateTopPadding()
                    ),
                    state = listState
                ) {
                    items(state.notes) { note ->
                        NoteItem(
                            modifier = Modifier.clickable {
                                viewModel.onEvent(NotesEvent.OpenOrCreateNote(note.id!!))
                                containerState = ContainerState.Fullscreen
//                            navController.navigate(NotesScreens.AddEditScreen.route + "?noteId=${note.id}")

                            }, title = note.title, content = note.content
                        )
                    }
                }

            }
            NotesFABContent(onClick = {
                viewModel.onEvent(NotesEvent.OpenOrCreateNote(null))
                containerState = ContainerState.Fullscreen
            }, containerState = containerState, onBackClick = { state ->
                containerState = state

            }, isExpanded = expandedFab
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior, onMenuClick: () -> Unit, onActionClick: () -> Unit
) {
    CenterAlignedTopAppBar(modifier = Modifier.padding(horizontal = 8.dp),
        title = { Text(text = "Notes") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Image(
                    painter = painterResource(id = R.drawable.profilephoto),
                    contentDescription = "Profile",
                    modifier = Modifier.clip(CircleShape)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

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

    transition.AnimatedContent(modifier = modifier
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
                AddEditNoteScreen1(onBackClick = { onBackClick(it) })
            }
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier, title: String = "", content: String = ""
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(0.5f)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary.copy(0.07f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = content,
                maxLines = 3,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground.copy(0.75f),
                textAlign = TextAlign.Justify
            )
        }
    }

}