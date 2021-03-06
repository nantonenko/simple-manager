package com.hfad.simplemanager.ui.TaskListScreen

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.TaskScreenEvent
import com.hfad.simplemanager.ui.components.ChangeValueMenu
import com.hfad.simplemanager.ui.components.Swapper
import com.hfad.simplemanager.ui.components.TransparentButton
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.round
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun TaskScreen(vm: TaskScreenVM = viewModel()) {
    val taskList by vm.taskFlow.collectAsState(listOf())
    val columnList by vm.taskListFlow.collectAsState(listOf())
    val selectedPrj by vm.selectedProjectFlow.collectAsState(initial = null)
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val w = LocalConfiguration.current.screenWidthDp

    if (selectedPrj == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.project_is_not_selected),
                modifier = Modifier.align(Alignment.Center),
                style = theme.typography.h5
            )
        }
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            columnList.forEachIndexed { index, taskListState ->
                Tab(
                    modifier = Modifier.height(48.dp),
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }) { Text(taskListState.title) }
            }
            Tab(
                modifier = Modifier.height(48.dp),
                selected = pagerState.currentPage == columnList.lastIndex + 1,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(columnList.lastIndex + 1) }
                }) {
                Text(stringResource(R.string.new_task_list))
            }
        }

        HorizontalPager(
            state = pagerState,
            count = columnList.size + 1,
            key = { i -> if (columnList.isNotEmpty() && i != columnList.lastIndex + 1) columnList[i].id else Int.MAX_VALUE }
        ) { page ->
            if (page == columnList.lastIndex + 1) {
                Box(modifier = Modifier.fillMaxSize()) {
                    NewTaskList(
                        Modifier
                            .width(w.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        vm.handleEvent(
                            TaskScreenEvent.NewTaskList(it)
                        )
                    }
                }
            } else {
                TaskList(
                    modifier = Modifier.width(w.dp),
                    state = columnList[page],
                    tasks = taskList,
                    destinations = columnList,
                    taskEventHandler = { vm.handleEvent(it) },
                    handle = { vm.handleEvent(it) }
                )
            }
        }
    }
}

@Composable
private fun NewTaskList(modifier: Modifier = Modifier, onNewTaskList: (String) -> Unit = {}) {
    var isTitleEnter by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.padding(theme.spacing.large),
        elevation = theme.elevation.large,
        shape = theme.shapes.round
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