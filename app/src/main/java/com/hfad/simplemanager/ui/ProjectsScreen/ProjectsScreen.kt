package com.hfad.simplemanager.ui.ProjectsScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hfad.simplemanager.ui.components.OutlinedTransparentButton
import com.hfad.simplemanager.ui.components.Swapper
import com.hfad.simplemanager.ui.components.TransparentButton
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun ProjectScreen(vm: ProjectScreenVM = viewModel()) {
    val prjs by vm.prjFlow.collectAsState(initial = listOf())
    LazyColumn(modifier = Modifier
        .padding(theme.spacing.large)
        .fillMaxSize()) {

        items(prjs) { prj ->
            Text(prj.name, style = theme.typography.h5)
        }

        item {
            OutlinedTransparentButton(
                modifier = Modifier.fillParentMaxWidth(),
                onClick = vm::createNewProject
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    }
}