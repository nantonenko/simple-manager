package com.hfad.simplemanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.taskListScreen.TaskState
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun TaskEditMenu(
    modifier: Modifier = Modifier,
    state: TaskState = TaskState(),
    onConfirm: (title: String, description: String, points: String) -> Unit = { _, _, _ -> },
    onCancel: () -> Unit = {}
) {
    var title by remember { mutableStateOf(state.title) }
    var description by remember { mutableStateOf(state.description) }
    var points by remember { mutableStateOf(if (state.points == 0) "" else state.points.toString()) }

    val fm = Modifier.fillMaxWidth()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(theme.spacing.large)
    ) {
        TextField(
            modifier = fm,
            value = title,
            onValueChange = { title = it },
            singleLine = true,
            label = { Text(stringResource(R.string.header)) })

        TextField(
            modifier = fm,
            value = points,
            onValueChange = { points = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(id = R.string.points)) })

        TextField(
            modifier = fm,
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) })

        ConfirmCancelButtons(
            onConfirm = { onConfirm(title, description, points) },
            onCancel = onCancel
        )
    }
}