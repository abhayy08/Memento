package com.abhay.memento.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.abhay.memento.di.AppModule.provideDrawerStateManager
import com.abhay.memento.navigation.AppScreen
import com.abhay.memento.navigation.HomeNavGraph
import kotlinx.coroutines.launch

@Composable
fun MementoAppScreen(
    navController: NavHostController
) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val currentDestination = currentRoute?.destination


    val drawerState = provideDrawerStateManager().drawerState
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val screens = listOf(
        AppScreen.Notes, AppScreen.Tasks, AppScreen.Reminders
    )

    DismissibleNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                selectedItem = selectedItemIndex,
                screens = screens,
                onClick = { index, route ->
                    selectedItemIndex = index
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(
                        route = route,
                    ) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        },
        drawerState = drawerState
    ) {
        HomeNavGraph(navController = navController)
    }
}

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    onClick: (Int, String) -> Unit = { _, _ -> },
    screens: List<AppScreen>,
) {
    DismissibleDrawerSheet(
        drawerShape = RoundedCornerShape(24.dp),
        drawerTonalElevation = 0.9.dp
    ) {
        Text(
            text = "Memento",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        screens.forEachIndexed { index, screen ->
            NavigationDrawerItem(
                modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                label = {
                    Text(text = screen.title)
                },
                selected = index == selectedItem,
                onClick = { onClick(index, screen.route) },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = screen.title)
                },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.3f)
        )
    }
}
