package com.hfad.simplemanager.ui.taskListScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hfad.simplemanager.Repository
import com.hfad.simplemanager.ui.Event
import com.hfad.simplemanager.ui.TaskEvent
import com.hfad.simplemanager.ui.TaskListEvent
import com.hfad.simplemanager.ui.TaskScreenEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.IllegalArgumentException

class TaskScreenVM : ViewModel() {

    lateinit var repo: Repository

    private val taskList = mutableListOf<TaskState>()
    private val columnList = mutableListOf<TaskListState>()

    private val _taskFlow = MutableStateFlow<List<TaskState>>(listOf())
    val taskFlow: StateFlow<List<TaskState>> get() = _taskFlow

    private val _columnFlow = MutableStateFlow<List<TaskListState>>(listOf())
    val columnFlow: StateFlow<List<TaskListState>> get() = _columnFlow

    private var columnId: Long = 0
        get() {
            return field++
        }

    private var taskId: Long = 0
        get() {
            return field++
        }

    init {
        updateColumns {
            columnList.add(TaskListState(id = columnId, title = "TODO"))
        }

        updateTasks {
            taskList.add(
                TaskState(
                    id = taskId,
                    listId = columnList.first().id,
                    title = "title 1",
                    description = "description",
                    points = 120
                )
            )
        }
    }

    fun handleEvent(e: Event) {
        when (e) {
            is TaskScreenEvent -> handleTaskScreenEvents(e)
            is TaskListEvent -> handleColumnEvent(e)
            is TaskEvent -> handleTaskEvents(e)
        }
    }

    /**
     * handle task screen events
     */

    private fun handleTaskScreenEvents(e: TaskScreenEvent) {
        when (e) {
            is TaskScreenEvent.NewTaskList -> updateColumns {
                columnList.add(TaskListState(id = columnId, title = e.title))
            }
        }
    }


    /**
     * handle task lsit events
     */

    private fun handleColumnEvent(e: TaskListEvent) {
        when (e) {
            is TaskListEvent.Delete -> updateColumns { columnList.removeAll { it.id == e.id } }
            is TaskListEvent.ChangeTitle -> changeColumnTitle(e.id, e.newTitle)
            is TaskListEvent.AddNewTask -> addNewTask(e)
        }
    }

    private fun changeColumnTitle(id: Long, title: String) = updateColumns {
        val i = getColumnIdx(id)
        columnList[i] = columnList[i].copy(title = title)
    }

    private fun addNewTask(e: TaskListEvent.AddNewTask) = updateTasks {
        val newTask = TaskState(
            id = taskId,
            listId = e.id,
            title = e.title,
            description = e.description,
            points = e.points
        )
        taskList.add(newTask)
        Log.i(TAG, "new task = $newTask")
    }

    private fun getColumnIdx(id: Long): Int {
        val i = columnList.indexOfFirst { it.id == id }
        if (i == 1) throw IllegalArgumentException("can't find column with given ID")
        return i
    }


    /**
     * handle task events
     */

    private fun handleTaskEvents(e: TaskEvent) {
        when (e) {
            is TaskEvent.Edit -> editTask(e)
            is TaskEvent.Delete -> deleteTask(e)
            is TaskEvent.ChangePoints -> changePoints(e)
            is TaskEvent.ChangeTitle -> changeTitle(e)
            is TaskEvent.Move -> moveTask(e)
        }
    }

    private fun moveTask(e: TaskEvent.Move) = updateTasks {
        val i = getTaskIdx(e.id)
        taskList[i] = taskList[i].copy(listId = e.destTaskListId)
    }

    private fun changeTitle(e: TaskEvent.ChangeTitle) = updateTasks {
        val i = getTaskIdx(e.id)
        taskList[i] = taskList[i].copy(title = e.newName)
    }

    private fun changePoints(e: TaskEvent.ChangePoints) = updateTasks {
        Log.i(TAG, "change points = ${e.id}")
        val i = getTaskIdx(e.id)
        taskList[i] = taskList[i].copy(points = e.newPoints)
    }

    private fun deleteTask(e: TaskEvent.Delete) = updateTasks {
        taskList.removeAt(getTaskIdx(e.id))
    }

    private fun editTask(e: TaskEvent.Edit) = updateTasks {
        val i = getTaskIdx(e.id)
        taskList[i] = taskList[i].copy(
            title = e.newName,
            description = e.newDescription,
            points = e.newPoints
        )
    }

    private fun getTaskIdx(id: Long): Int {
        val i = taskList.indexOfFirst { it.id == id }
        if (i == -1) throw IllegalArgumentException("can't find task with given ID")
        return i
    }


    /**
     * functions to update flows
     */

    private fun updateColumns(listMutation: () -> Unit): Unit {
        listMutation()
        _columnFlow.value = columnList.toList()
    }

    private fun updateTasks(listMutation: () -> Unit): Unit {
        listMutation()
        _taskFlow.value = taskList.toList()
    }

    companion object {
        const val TAG = "TaskScreenVM"
    }
}