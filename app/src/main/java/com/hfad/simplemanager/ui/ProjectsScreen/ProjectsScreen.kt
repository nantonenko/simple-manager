package com.hfad.simplemanager.ui.ProjectsScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hfad.simplemanager.R
import com.hfad.simplemanager.dataBase.ProjectEntity
import com.hfad.simplemanager.ui.components.ConfirmCancelButtons
import com.hfad.simplemanager.ui.components.OutlinedTransparentButton
import com.hfad.simplemanager.ui.components.Swapper
import com.hfad.simplemanager.ui.components.TransparentButton
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.round
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun ProjectScreen(vm: ProjectScreenVM = viewModel()) {
    val prjs by vm.prjFlow.collectAsState(initial = listOf())

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ProjectList(
            prjs = prjs,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = theme.spacing.medium)
                .fillMaxSize(),
            onProjectEdit = { e -> vm.updateProject(e) },
            onProjectSelected = vm::onProjectSelected,
            onProjectDeleted = vm::onProjectDeleted
        )

        TransparentButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = theme.colors.surface),
            onClick = vm::createNewProject,
            color = theme.colors.primaryVariant.copy(alpha = 0.3f)
        ) {
            Icon(Icons.Default.Add, null)
        }

    }
}

@Composable
private fun ProjectList(
    modifier: Modifier = Modifier,
    prjs: List<ProjectEntity>,
    onProjectEdit: (prjEntity: ProjectEntity) -> Unit,
    onProjectSelected: (prjEntity: ProjectEntity) -> Unit,
    onProjectDeleted: (prjEntity: ProjectEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(theme.spacing.large)
    ) {
        item {
            Spacer(modifier = Modifier.height(theme.spacing.medium))
        }
        items(prjs) { prj ->
            ProjectCard(
                modifier = Modifier.fillMaxWidth(),
                name = prj.name,
                isSelected = prj.isSelected,
                description = prj.description,
                onProjectEdit = { name, descr ->
                    onProjectEdit(
                        prj.copy(
                            name = name,
                            description = descr
                        )
                    )
                },
                onProjectSelected = { onProjectSelected(prj) },
                onProjectDeleted = { onProjectDeleted(prj) }
            )
        }
    }
}

@Composable
private fun ProjectCard(
    modifier: Modifier = Modifier,
    name: String = "",
    description: String = "",
    isSelected: Boolean = false,
    onProjectEdit: (name: String, descr: String) -> Unit,
    onProjectSelected: () -> Unit,
    onProjectDeleted: () -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }
    val cardBoarderColor by animateColorAsState(
        targetValue = if (isSelected) theme.colors.primary.copy(alpha = 0.6f)
        else theme.colors.surface
    )
    val cardColor by animateColorAsState(
        targetValue = if (isSelected) theme.colors.primary.copy(alpha = 0.05f)
        else theme.colors.surface
    )

    Card(
        modifier = modifier,
        elevation = theme.elevation.large,
        shape = theme.shapes.round,
        border = BorderStroke(2.dp, color = cardBoarderColor),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = cardColor)) {
            Swapper(
                key = isEditMode,
                isContinues = true,
                enter = expandVertically(tween(350, easing = LinearEasing)) { -it },
                exit = shrinkVertically(tween(350, easing = LinearEasing)) { -it }
            ) {
                if (it) {
                    ProjectCardEdit(
                        name = name,
                        description = description,
                        onAccept = { name, descr ->
                            onProjectEdit(name, descr); isEditMode = false
                        },
                        onCancel = { isEditMode = false }
                    )
                } else {
                    ProjectCardMain(
                        name = name,
                        description = description,
                        onEditClicked = { isEditMode = true },
                        onProjectSelected = onProjectSelected,
                        onProjectDeleted = onProjectDeleted
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectCardMain(
    name: String,
    description: String,
    onEditClicked: () -> Unit,
    onProjectSelected: () -> Unit,
    onProjectDeleted: () -> Unit,
) {
    var isDescriptionOpened by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onEditClicked) {
                Icon(Icons.Default.Edit, null)
            }

            Text(name, style = theme.typography.h5, textAlign = TextAlign.Start)

            IconButton(onClick = { isDescriptionOpened = !isDescriptionOpened }) {
                Icon(
                    Icons.Default.ArrowDropDown, null, modifier = Modifier.rotate(
                        if (isDescriptionOpened) 180f else 0f
                    )
                )
            }
        }

        AnimatedVisibility(visible = isDescriptionOpened) {
            Divider()
            Column {
                Spacer(modifier = Modifier.height(theme.spacing.extraLarge))
                OutlinedTransparentButton(
                    onClick = onProjectSelected,
                    modifier = Modifier
                        .padding(horizontal = theme.spacing.corner)
                        .fillMaxWidth()
                ) {
                    Text(stringResource(R.string.select))
                }
                Spacer(modifier = Modifier.height(theme.spacing.medium))
                OutlinedTransparentButton(
                    onClick = onProjectDeleted,
                    modifier = Modifier
                        .padding(horizontal = theme.spacing.corner)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.delete), color = theme.colors.error)
                }
                Spacer(modifier = Modifier.height(theme.spacing.extraLarge))

                if (description.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = theme.spacing.corner)
                            .padding(bottom = theme.spacing.corner)
                    ) {
                        Text(stringResource(id = R.string.description), style = theme.typography.h6)
                        Spacer(modifier = Modifier.height(theme.spacing.medium))
                        Text(description)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectCardEdit(
    name: String,
    description: String,
    onAccept: (name: String, descr: String) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {}
) {
    var newName by remember { mutableStateOf(name) }
    var newDescription by remember { mutableStateOf(description) }
    Column {
        TextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text(stringResource(R.string.name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text(stringResource(id = R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(theme.spacing.medium))

        ConfirmCancelButtons(onConfirm = { onAccept(newName, newDescription) }, onCancel = onCancel)
    }
}