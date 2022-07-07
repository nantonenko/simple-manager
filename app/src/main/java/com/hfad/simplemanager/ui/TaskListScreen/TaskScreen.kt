package com.hfad.simplemanager.ui.TaskListScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hfad.simplemanager.ui.TaskScreenEvent
import com.hfad.simplemanager.ui.components.ChangeValueMenu
import com.hfad.simplemanager.ui.components.Swapper
import com.hfad.simplemanager.ui.components.TransparentButton
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme


@Composable
fun TaskScreen(vm: TaskScreenVM = viewModel()) {
    val taskList by vm.taskFlow.collectAsState()
    val columnList by vm.columnFlow.collectAsState()

    val w = LocalConfiguration.current.screenWidthDp

    LazyRow(
        modifier = Modifier
            .padding(theme.spacing.medium)
            .fillMaxSize()
    ) {
        items(columnList) { c ->
            TaskList(
                modifier = Modifier.width(w.dp),
                state = c,
                tasks = taskList,
                destinations = columnList,
                taskEventHandler = { vm.handleEvent(it) },
                handle = { vm.handleEvent(it) }
            )
        }

        item {
            NewTaskList(Modifier.width(w.dp)) { vm.handleEvent(TaskScreenEvent.NewTaskList(it)) }
        }
    }
}

@Composable
private fun NewTaskList(modifier: Modifier = Modifier, onNewTaskList: (String) -> Unit = {}) {
    var isTitleEnter by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.padding(theme.spacing.large),
        elevation = theme.elevation.large
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Swapper(
                key = isTitleEnter,
                isContinues = true,
                enter = expandVertically(tween(350, easing = LinearEasing)) { -it },
                exit = shrinkVertically(tween(350, easing = LinearEasing)) { -it }
            ) {
                if (it) {
                    ChangeValueMenu(
                        modifier = Modifier.fillMaxWidth(),
                        value = "",
                        onConfirm = { newTitle ->
                            isTitleEnter = false
                            onNewTaskList(newTitle)
                        },
                        onCancel = { isTitleEnter = false }
                    )
                } else {
                    TransparentButton(
                        onClick = { isTitleEnter = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            }
        }
    }
}