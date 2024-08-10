package com.abhay.features.note.note_ui.notes

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhay.common.DrawerStateManager
import com.abhay.features.note.note_ui.R
import com.abhay.features.note.note_ui.components.NoteItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesUiScreen(
    viewModel: NotesViewModel,
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

