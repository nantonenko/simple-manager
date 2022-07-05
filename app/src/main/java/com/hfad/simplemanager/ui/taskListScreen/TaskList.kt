package com.hfad.simplemanager.ui.taskListScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.TaskEvents
import com.hfad.simplemanager.ui.TaskListEvents
import com.hfad.simplemanager.ui.components.*
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.round
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme

data class TaskListState(
    val id: Int = 0,
    val projectId: Int = 0,
    val title: String = ""
)

private enum class TlState { MAIN, RENAME, MENU, DELETE_CONFIRMATION }

/**
 * [TaskList] main composable of task list
 * [MenuState] state with menu (rename and delete)
 */

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    state: TaskListState = TaskListState(),
    tasks: List<TaskState> = listOf(),
    taskEventHandler: (TaskEvents) -> Unit = {},
    handle: (TaskListEvents) -> Unit = {}
) {
    var tlState by remember { mutableStateOf(TlState.MAIN) }
    val menuIconDegree: Float by animateFloatAsState(if (tlState == TlState.MAIN) 0f else 90f)

    fun main() {
        tlState = TlState.MAIN
    }

    fun rename() {
        tlState = TlState.RENAME
    }

    fun menu() {
        tlState = TlState.MENU
    }

    fun delete() {
        tlState = TlState.DELETE_CONFIRMATION
    }

    Card(
        modifier = modifier.padding(theme.spacing.large),
        shape = theme.shapes.round,
        elevation = if (isSystemInDarkTheme()) theme.elevation.small else theme.elevation.large
    ) {
        Box {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .padding(
                        start = theme.spacing.extraLarge,
                        end = theme.spacing.extraLarge,
                        top = theme.spacing.large,
                        bottom = theme.spacing.no
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(state.title, style = theme.typography.h4)
                    IconButton(onClick = { if (tlState == TlState.MAIN) menu() else main() }) {
                        Icon(
                            Icons.Default.Menu, null, modifier = Modifier.rotate(menuIconDegree)
                        )
                    }
                }

                Swapper(
                    key = tlState,
                    isContinues = true,
                    enter = expandVertically(tween(350, easing = LinearEasing)) { -it },
                    exit = shrinkVertically(tween(350, easing = LinearEasing)) { -it }
                ) { key ->
                    when (key) {
                        TlState.MAIN -> {}
                        TlState.RENAME -> {
                            ChangeValueMenu(
                                value = state.title,
                                onConfirm = { newTitle ->
                                    handle(TaskListEvents.ChangeTitle(state.id, newTitle))
                                    main()
                                },
                                onCancel = { main() })
                        }
                        TlState.MENU -> {
                            MenuState(
                                onRename = { rename() },
                                onDelete = { delete() }
                            )
                        }
                        TlState.DELETE_CONFIRMATION -> {
                            DeleteConfirmation(
                                onConfirm = {
                                    handle(TaskListEvents.Delete(state.id))
                                    main()
                                },
                                onCancel = { main() }
                            )
                        }
                    }
                }

                Divider()
                Spacer(modifier = Modifier.height(theme.spacing.extraLarge))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(theme.spacing.large),
                    modifier = Modifier.weight(1f)
                ) {
                    items(tasks.filter { it.listId == state.id }) { task ->
                        Task(
                            modifier = Modifier.fillMaxWidth(),
                            state = task,
                            handle = taskEventHandler
                        )
                    }
                }
            }

            var isNewTaskNameEdit by remember { mutableStateOf(false) }

            Swapper(
                key = isNewTaskNameEdit,
                enter = expandVertically(tween(350, easing = LinearEasing)) { -it },
                exit = shrinkVertically(tween(350, easing = LinearEasing)) { -it },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                if (it) {
                    TaskEditMenu(
                        onCancel = { isNewTaskNameEdit = false },
                        onConfirm = {_,_,_ -> isNewTaskNameEdit = false},
                        modifier = Modifier.background(color = theme.colors.surface)
                    )
                } else {
                    TransparentButton(
                        onClick = { isNewTaskNameEdit = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        color = theme.colors.surface
                    ) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            }
        }
    }
}


@Composable
private fun MenuState(
    modifier: Modifier = Modifier,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        TransparentButton(onClick = onRename, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(id = R.string.rename), style = theme.typography.h6)
        }
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.medium))
        LeadingIconButton(
            onClick = onDelete,
            icon = { Icon(Icons.Default.Delete, null, tint = theme.colors.error) },
            text = {
                Text(
                    stringResource(id = R.string.delete),
                    style = theme.typography.h6.copy(color = theme.colors.error)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskList() {
    TaskList(
        state = TaskListState(id = 0, title = "TODO"),
        tasks = listOf(
            TaskState(
                id = 0,
                title = "task 1",
                description = "some description 1",
                points = 1 * 100
            ),
            TaskState(
                id = 0,
                title = "task 2",
                description = "some description 2",
                points = 2 * 100
            ),
            TaskState(
                id = 0,
                title = "task 3",
                description = "some description 3",
                points = 3 * 100
            ),
            TaskState(
                id = 0,
                title = "task 4",
                description = "some description 4",
                points = 4 * 100
            ),
            TaskState(
                id = 0,
                title = "task 5",
                description = "some description 5",
                points = 5 * 100
            ),
        )
    )
}