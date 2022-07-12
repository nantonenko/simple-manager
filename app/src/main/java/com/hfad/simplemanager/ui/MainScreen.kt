package com.hfad.simplemanager.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.ProjectsScreen.ProjectScreen
import com.hfad.simplemanager.ui.ProjectsScreen.ProjectScreenVM
import com.hfad.simplemanager.ui.TaskListScreen.TaskScreen
import com.hfad.simplemanager.ui.TaskListScreen.TaskScreenVM
import com.hfad.simplemanager.ui.theme.theme
import java.util.stream.Collectors.toList

private enum class Destinations(@StringRes val text: Int, @DrawableRes val icon: Int) {
    Tasks(R.string.task_lists, R.drawable.ic_baseline_view_task_list_24),
    Projects(R.string.projects, R.drawable.ic_baseline_project_24)
}

@Composable
fun MainScreen(
    taskScreenVm: TaskScreenVM,
    projectScreenVm: ProjectScreenVM
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val systemBarColor = theme.colors.surface

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = systemBarColor,
            darkIcons = useDarkIcons
        )
    }
    var screen by remember { mutableStateOf(Destinations.Projects) }

    Scaffold(
        bottomBar = {
            BottomNavigation() {
                Destinations.values().forEach { dest ->
                    BottomNavigationItem(
                        selected = dest == screen,
                        onClick = { screen = dest },
                        label = { Text(stringResource(id = dest.text)) },
                        icon = { Icon(painterResource(id = dest.icon), null) }
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (screen) {
                Destinations.Tasks -> TaskScreen(taskScreenVm)
                Destinations.Projects -> ProjectScreen(projectScreenVm)
            }
        }
    }
}