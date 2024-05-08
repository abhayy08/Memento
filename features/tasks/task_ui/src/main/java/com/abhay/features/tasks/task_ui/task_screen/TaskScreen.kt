package com.abhay.features.tasks.task_ui.task_screen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.abhay.common.DrawerStateManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    drawerStateManager: DrawerStateManager
) {

    val tabs = listOf("Fav", "My Tasks", "List 1")

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val pagerState = rememberPagerState {
        tabs.size
    }

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }
    val scope = rememberCoroutineScope()

    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TaskTopAppBar(
                    scrollBehavior = scrollBehavior,
                    onMenuClick = { scope.launch {
                        drawerStateManager.openDrawer()
                    }},
                    onActionClick = {}
                )
            },
            floatingActionButton = { TaskScreenFAB() }
        ) {paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabPositions ->
                        //https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Tab(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Function0,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.interaction.MutableInteractionSource)
                        AnimatedIndicator(selectedTab = selectedTabIndex, tabPositions = tabPositions)
                    },
                    edgePadding = 40.dp,
                    divider = {Divider(modifier = Modifier.fillMaxWidth())}
                ) {
                    tabs.forEachIndexed {index, tabName ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(text = tabName)
                            }
                        )
                    }
                    TextButton(onClick = {  }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add List")
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { index ->
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = tabs[index])
                    }
                }
            }

        }
    }
}

@Composable
fun AnimatedIndicator(
    selectedTab: Int, tabPositions: List<TabPosition>
) {
    val transition = updateTransition(targetState = selectedTab, label = "indicator_animation")

    val indicatorStart by transition.animateDp(
        transitionSpec = {
            val direction = targetState - initialState
            when (direction) {
                in 1..Int.MAX_VALUE -> spring(dampingRatio = 1f, stiffness = 100f)
                in Int.MIN_VALUE..-1 -> spring(dampingRatio = 1f, stiffness = 1000f)
                else -> spring(dampingRatio = 1f, stiffness = 500f)
            }
        }, label = "fancy_indicator"
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            val direction = targetState - initialState
            when (direction) {
                in 1..Int.MAX_VALUE -> spring(dampingRatio = 1f, stiffness = 1000f)
                in Int.MIN_VALUE..-1 -> spring(dampingRatio = 1f, stiffness = 50f)
                else -> spring(dampingRatio = 1f, stiffness = 500f)
            }
        }, label = "indicator_position"
    ) {
        tabPositions[it].right
    }
    TabRowDefaults.Indicator(modifier = Modifier
        .fillMaxSize(0.8f)
        .wrapContentSize(align = Alignment.BottomStart)
        .offset { IntOffset(x = indicatorStart.roundToPx() + 16.dp.roundToPx(), y = 0) }
        .width((indicatorEnd) - indicatorStart - 32.dp))
}

@Composable
fun TaskScreenFAB() {
    ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Add Task")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior, onMenuClick: () -> Unit, onActionClick: () -> Unit
) {
    CenterAlignedTopAppBar(modifier = Modifier.padding(horizontal = 8.dp),
        title = { Text(text = "Tasks") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {

            }
        },
        scrollBehavior = scrollBehavior
    )
}


@Preview
@Composable
private fun TaskPreview() {
    TaskScreen(drawerStateManager = DrawerStateManager)
}