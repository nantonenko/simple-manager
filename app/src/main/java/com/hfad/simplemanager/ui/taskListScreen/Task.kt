package com.hfad.simplemanager.ui.taskListScreen

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.TaskEvent
import com.hfad.simplemanager.ui.components.*
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.round
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme

data class TaskState(
    val id: Long = 0,
    val listId: Long = 0, // id of task list to which this task is belong
    val projectId: Long = 0, // id of project to which this task is belong
    val title: String = "",
    val description: String = "",
    val points: Int = 0
)

private const val MAX_DESCRIPTION_LINES = 5 // characters

private enum class TState { MAIN, MENU, RENAME, MOVE_MENU, POINTS_EDIT, EDIT, DELETE_CONFIRMATION }

/**
 * Navigation
 * [Task] main composable
 * [BackButton] back button for different menues
 * [MenuState] menu state of task card
 * [Description] container for description
 * [DescriptionFull] fully opened description with all text visible
 * [DescriptionPartial] description with [MAX_DESCRIPTION_LINES] visible
 * [MoveMenu] menu with possible destination task lists
 */

@Composable
fun Task(
    modifier: Modifier = Modifier,
    state: TaskState = TaskState(),
    destinations: List<TaskListState> = listOf(),
    handle: (TaskEvent) -> Unit = {}
) {
    var taskState by remember { mutableStateOf(TState.MAIN) }

    fun back() {
        taskState = TState.MAIN
    }

    fun rename() {
        taskState = TState.RENAME
    }

    fun edit() {
        taskState = TState.EDIT
    }

    fun move() {
        taskState = TState.MOVE_MENU
    }

    fun delete() {
        taskState = TState.DELETE_CONFIRMATION
    }

    fun editPoints() {
        taskState = TState.POINTS_EDIT
    }

    fun menu() {
        taskState = TState.MENU
    }

    Card(
        modifier = modifier.animateContentSize(),
        shape = theme.shapes.round,
        elevation = if (isSystemInDarkTheme()) theme.elevation.large else theme.elevation.small,
        border = BorderStroke(width = 2.dp, color = theme.colors.onSurface.copy(alpha = 0.25f))
    ) {
        Swapper(
            key = taskState,
            isContinues = true,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { -it }) { ts ->
            when (ts) {
                TState.MAIN -> {
                    MainState(
                        state = state,
                        onOpenMenu = ::menu,
                        onEdit = ::edit,
                        onDelete = ::delete,
                        onEditPoints = ::editPoints
                    )
                }
                TState.MENU -> {
                    MenuState(
                        onBack = ::back,
                        onRename = ::rename,
                        onEdit = ::edit,
                        onEditPoints = ::editPoints,
                        onMove = ::move,
                        onDelete = ::delete
                    )
                }
                TState.RENAME -> {
                    ChangeValueMenu(
                        value = state.title,
                        label = { Text(stringResource(R.string.new_header)) },
                        onConfirm = { newTitle ->
                            handle(TaskEvent.ChangeTitle(state.id, newTitle))
                            back()
                        },
                        onCancel = ::back
                    )
                }
                TState.MOVE_MENU -> {
                    MoveMenu(
                        destinations = destinations.filter { it.id != state.listId },
                        onBack = ::back,
                        onDestinationSelected = { dest ->
                            handle(TaskEvent.Move(state.id, dest))
                            back()
                        }
                    )
                }
                TState.POINTS_EDIT -> {
                    ChangeValueMenu(
                        value = state.points.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(id = R.string.points)) },
                        onConfirm = { newPoints ->
                            handle(
                                TaskEvent.ChangePoints(
                                    state.id,
                                    if (newPoints.isBlank() || newPoints.toIntOrNull() == null) 0 else newPoints.toInt()
                                )
                            )
                            back()
                        },
                        onCancel = ::back,
                    )
                }
                TState.EDIT -> {
                    TaskEditMenu(state = state, onConfirm = { nn, nd, np ->
                        handle(
                            TaskEvent.Edit(
                                id = state.id,
                                newName = nn,
                                newDescription = nd,
                                newPoints = if (np.isBlank() || np.toIntOrNull() == null) 0 else np.toInt()
                            )
                        )
                        back()
                    }, onCancel = ::back)
                }
                TState.DELETE_CONFIRMATION -> {
                    DeleteConfirmation(
                        onConfirm = {
                            handle(TaskEvent.Delete(state.id))
                            back()
                        },
                        onCancel = ::back
                    )
                }
            }
        }
    }
}

@Composable
private fun MainState(
    state: TaskState,
    onOpenMenu: () -> Unit = {},
    onEdit: () -> Unit,
    onEditPoints: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = theme.spacing.corner, vertical = theme.spacing.medium)
            .fillMaxWidth()
    ) {
        Text(text = state.title, style = theme.typography.h6)
        Divider()

        Spacer(modifier = Modifier.height(theme.spacing.medium))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    null,
                    tint = theme.colors.error
                )
            }

            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null) }

            IconButton(onClick = onOpenMenu) { Icon(Icons.Default.Menu, null) }

            OutlinedTransparentButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = onEditPoints
            ) {
                Text(state.points.toString(), style = theme.typography.h5)
            }
        }
        Spacer(modifier = Modifier.height(theme.spacing.medium))
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.large))
        Description(text = state.description)
    }
}

@Composable
private fun Description(text: String) {
    var textKey by remember { mutableStateOf(0) }
    Swapper(key = textKey, enter = fadeIn(), exit = fadeOut()) { key ->
        when (key) {
            0 -> DescriptionPartial(text = text) { textKey = 1 }
            1 -> DescriptionFull(text = text) { textKey = 0 }
        }
    }
}

@Composable
private fun DescriptionPartial(text: String, onOpen: () -> Unit) = Text(
    text = text,
    maxLines = MAX_DESCRIPTION_LINES,
    style = theme.typography.body1,
    overflow = TextOverflow.Ellipsis,
    modifier = Modifier.pointerInput(Unit) {
        var vd = false
        detectVerticalDragGestures(onVerticalDrag = { _, d ->
            vd = d > 0
        }, onDragEnd = {
            if (vd) {
                onOpen()
                vd = false
            }
        })
    })

@Composable
private fun DescriptionFull(text: String, onClose: () -> Unit) = Text(
    text = text,
    style = theme.typography.body1,
    overflow = TextOverflow.Ellipsis,
    modifier = Modifier.pointerInput(Unit) {
        var vd = false
        detectVerticalDragGestures(onVerticalDrag = { _, d ->
            vd = d < 0
        }, onDragEnd = { if (vd) onClose() })
    })

@Composable
private fun MenuState(
    onBack: () -> Unit = {},
    onRename: () -> Unit = {},
    onEdit: () -> Unit = {},
    onEditPoints: () -> Unit = {},
    onMove: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val btnMod = Modifier.fillMaxWidth()
    val btnStyle = theme.typography.h6
    val deleteStyle = btnStyle.copy(color = theme.colors.error)
    Column(
        modifier = Modifier.padding(
            horizontal = theme.spacing.corner,
            vertical = theme.spacing.medium
        )
    ) {
        BackButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = onBack)
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.small))

        TransparentButton(modifier = btnMod, onClick = onRename) {
            Text(stringResource(R.string.rename), style = btnStyle)
        }

        TransparentButton(modifier = btnMod, onClick = onEdit) {
            Text(stringResource(R.string.edit), style = btnStyle)
        }

        TransparentButton(modifier = btnMod, onClick = onEditPoints) {
            Text(stringResource(R.string.edit_points), style = btnStyle)
        }

        TransparentButton(modifier = btnMod, onClick = onMove) {
            Text(stringResource(R.string.move), style = btnStyle)
        }
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.small))

        LeadingIconButton(
            onClick = onDelete,
            icon = { Icon(Icons.Default.Delete, null, tint = deleteStyle.color) },
            text = { Text(stringResource(R.string.delete), style = deleteStyle) })
    }
}

@Composable
private fun MoveMenu(
    modifier: Modifier = Modifier,
    destinations: List<TaskListState> = listOf(),
    onDestinationSelected: (id: Long) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier) {
        BackButton(onClick = onBack)
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.large))

        if (destinations.isEmpty()) {
            Text(
                modifier = Modifier.padding(theme.spacing.corner),
                text = stringResource(R.string.no_available_task_lists_to_move),
                style = theme.typography.body1
            )
        } else {
            destinations.forEach { dest ->
                TransparentButton(
                    onClick = { onDestinationSelected(dest.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = theme.spacing.corner),
                        text = dest.title,
                        style = theme.typography.h6
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTask() {
    val s by remember {
        mutableStateOf(
            TaskState(
                title = "Make card with product description and so on and so on",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem" + " Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown" + " printer took a galley of type and scrambled it to make a type specimen book. It has survived " + "not only five centuries, but also the leap into electronic typesetting, remaining essentially " + "unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem " + "Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including " + "versions of Lorem Ipsum.",
                points = 1000
            )
        )
    }
    Surface(modifier = Modifier) {
        Task(
            modifier = Modifier
                .fillMaxWidth()
                .padding(theme.spacing.corner)
                .wrapContentHeight(), state = s
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuState() {
    MenuState()
}

@Preview(showBackground = true)
@Composable
private fun PreviewEditMenuState() {
    TaskEditMenu()
}