package com.hfad.simplemanager.ui.taskListScreen

import android.util.EventLogTags
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PointMode.Companion.Points
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.components.OutlinedTransparentButton
import com.hfad.simplemanager.ui.components.Swapper
import com.hfad.simplemanager.ui.components.TransparentButton
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.round
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TaskState(
    val id: Int = 0,
    val listId: Int = 0, // id of task list to which this task is belong
    val projectId: Int = 0, // id of project to which this task is belong
    val title: String = "",
    val description: String = "",
    val points: Int = 0
)

private const val MAX_DESCRIPTION_LINES = 5 // characters

private enum class State { MAIN, MENU, RENAME, MOVE_MENU, POINTS_EDIT, DELETE_CONFIRMATION }

@Composable
fun Task(
    modifier: Modifier = Modifier,
    state: TaskState = TaskState()
) {
    var taskState by remember { mutableStateOf(State.MAIN) }
    Card(
        modifier = modifier.animateContentSize(),
        shape = theme.shapes.round,
        elevation = theme.elevation.small,
        border = BorderStroke(width = 2.dp, color = theme.colors.onSurface.copy(alpha = 0.25f))
    ) {
        Swapper(
            key = taskState,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { -it }
        ) { ts ->
            when (ts) {
                State.MAIN -> MainState(
                    state = state,
                    onOpenMenu = { taskState = State.MENU },
                    onEdit = { taskState = State.MENU },
                    onEditPoints = { taskState = State.POINTS_EDIT }
                )
                State.MENU -> MenuState(
                    onBack = { taskState = State.MAIN },
                    onRename = { taskState = State.RENAME },
                    onEdit = {},
                    onMove = { taskState = State.MOVE_MENU },
                    onDelete = { taskState = State.DELETE_CONFIRMATION }
                )
                State.RENAME -> {}
                State.MOVE_MENU -> {}
                State.POINTS_EDIT -> {}
                State.DELETE_CONFIRMATION -> {}
            }
        }
    }
}

@Composable
private fun MainState(state: TaskState, onOpenMenu: () -> Unit = {}, onEdit: () -> Unit, onEditPoints: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .padding(horizontal = theme.spacing.corner, vertical = theme.spacing.medium)
            .fillMaxWidth()
    ) {
        Text(
            text = state.title,
            style = theme.typography.h6
        )
        Divider()

        Spacer(modifier = Modifier.height(theme.spacing.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            IconButton(onClick = {}) {
                Icon(Icons.Default.Edit, null)
            }

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
private fun Back(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val backStyle = theme.typography.h6.copy(color = theme.typography.h6.color.copy(alpha = 0.6f))
    TransparentButton(modifier = modifier, onClick = onClick) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.ArrowBack, null, tint = backStyle.color, modifier = Modifier.align(Alignment.CenterStart))
            Text(stringResource(R.string.back), style = backStyle, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun MenuState(
    onBack: () -> Unit = {},
    onRename: () -> Unit = {},
    onEdit: () -> Unit = {},
    onMove: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val btnMod = Modifier.fillMaxWidth()
    val btnStyle = theme.typography.h6
    val deleteStyle = btnStyle.copy(color = theme.colors.error)
    Column(
        modifier = Modifier.padding(horizontal = theme.spacing.corner, vertical = theme.spacing.medium)
    ) {
        Back(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = onBack)
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.small))

        TransparentButton(modifier = btnMod, onClick = onRename) {
            Text(stringResource(R.string.rename), style = btnStyle)
        }

        TransparentButton(modifier = btnMod, onClick = onEdit) {
            Text(stringResource(R.string.edit), style = btnStyle)
        }

        TransparentButton(modifier = btnMod, onClick = onMove) {
            Text(stringResource(R.string.move), style = btnStyle)
        }
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.small))

        TransparentButton(modifier = btnMod, onClick = onDelete) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Delete, null, tint = deleteStyle.color, modifier = Modifier.align(Alignment.CenterStart))
                Text(stringResource(R.string.delete), style = deleteStyle, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuState() {
    MenuState()
}

@Composable
private fun Description(text: String) {
    var textKey by remember { mutableStateOf(0) }
    Swapper(
        key = textKey,
        enter = fadeIn(),
        exit = fadeOut()
    ) { key ->
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
        detectVerticalDragGestures(
            onVerticalDrag = { _, d ->
                vd = d > 0
            },
            onDragEnd = {
                if (vd) {
                    onOpen()
                    vd = false
                }
            }
        )
    }
)

@Composable
private fun DescriptionFull(text: String, onClose: () -> Unit) = Text(
    text = text,
    style = theme.typography.body1,
    overflow = TextOverflow.Ellipsis,
    modifier = Modifier.pointerInput(Unit) {
        var vd = false
        detectVerticalDragGestures(
            onVerticalDrag = { _, d ->
                vd = d < 0
            },
            onDragEnd = { if (vd) onClose() }
        )
    }
)

@Preview(showBackground = true)
@Composable
private fun PreviewTask() {
    val s by remember {
        mutableStateOf(
            TaskState(
                title = "Make card with product description and so on and so on",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem" +
                        " Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown" +
                        " printer took a galley of type and scrambled it to make a type specimen book. It has survived " +
                        "not only five centuries, but also the leap into electronic typesetting, remaining essentially " +
                        "unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem " +
                        "Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including " +
                        "versions of Lorem Ipsum.",
                points = 1
            )
        )
    }
    Surface(modifier = Modifier) {
        Task(
            modifier = Modifier
                .fillMaxWidth()
                .padding(theme.spacing.corner)
                .wrapContentHeight(),
            state = s
        )
    }
}