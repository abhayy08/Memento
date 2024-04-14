package com.abhay.features.note.note_ui.notes

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.abhay.features.note.note_ui.R
import com.abhay.features.note.note_ui.navigation.NotesScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesUiScreen(
    viewModel: NotesViewModel,
    navController: NavHostController,
) {
    val state = viewModel.notesState.value
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { NotesFAB(onClick = { navController.navigate(route = NotesScreens.AddEditScreen.route) }) },
        topBar = { NoteTopAppBar(
            scrollBehavior,
            onMenuClick = {

            },
            onActionClick = {
                Toast.makeText(context,"You tapped your Profile", Toast.LENGTH_SHORT).show()
            }
        ) }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            columns = StaggeredGridCells.Adaptive(156.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(state.notes) { note ->
                NoteItem(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(NotesScreens.AddEditScreen.route + "?noteId=${note.id}")
                        },
                    title = note.title,
                    content = note.content
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClick: () -> Unit,
    onActionClick: () -> Unit
) {
    CenterAlignedTopAppBar(
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
fun NotesFAB(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(onClick = onClick) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Add Note")
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    title: String = "",
    content: String = ""
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
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
            }
            Spacer(modifier = Modifier.height(16.dp))
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