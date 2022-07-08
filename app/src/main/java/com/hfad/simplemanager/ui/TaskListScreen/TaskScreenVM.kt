package com.hfad.simplemanager.ui.TaskListScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.simplemanager.dataBase.*
import com.hfad.simplemanager.ui.Event
import com.hfad.simplemanager.ui.TaskEvent
import com.hfad.simplemanager.ui.TaskListEvent
import com.hfad.simplemanager.ui.TaskScreenEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

class TaskScreenVM(db: AppDatabase) : ViewModel() {

    private val projectDao = db.projectDao()
    private val taskListDao = db.taskListDao()
    private val taskDao = db.taskDao()

    private val taskList = mutableListOf<TaskState>()
    private val columnList = mutableListOf<TaskListState>()

    var taskFlow: Flow<List<TaskState>> = flow { emit(listOf()) }
        private set

    var taskListFlow: Flow<List<TaskListState>> = flow { emit(listOf()) }
        private set

    val selectedProjectFlow = projectDao.getSelectedProject()

    private var columnId: Long = 0
        get() {
            return field++
        }

    private var taskId: Long = 0
        get() {
            return field++
        }

    init {
        ioLaunch {
            val sp = projectDao.getSelectedProjectSync() ?: return@ioLaunch
        }

        ioLaunch {
            selectedProjectFlow.filterNotNull().collect { prj -> updateFlow(prj.id) }
        }
    }

    private fun updateFlow(projectId: Long) {
        taskListFlow =
            taskListDao.getAllForProject(projectId).map { e -> e.map { taskListEntityToState(it) } }

        taskFlow = taskDao.getAllForProject(projectId).map { e -> e.map { taskEntityToState(it) } }
    }


    /**
     * event handling
     */

    fun handleEvent(e: Event) {
        when (e) {
            is TaskScreenEvent -> handleTaskScreenEvents(e)
            is TaskListEvent -> handleTaskListEvents(e)
            is TaskEvent -> handleTaskEvents(e)
        }
    }


    /**
     * handle task screen events
     */

    private fun handleTaskScreenEvents(e: TaskScreenEvent) {
        when (e) {
            is TaskScreenEvent.NewTaskList -> ioLaunch {
                taskListDao.insert(TaskListEntity(
                    projectId = projectDao.getSelectedProjectSync()!!.id,
                    title = e.title
                ))
            }
        }
    }


    /**
     * handle task lsit events
     */

    private fun handleTaskListEvents(e: TaskListEvent) {
        when (e) {
            is TaskListEvent.Delete -> ioLaunch { taskListDao.deleteById(e.id) }
            is TaskListEvent.ChangeTitle -> changeTaskListTitle(e.id, e.newTitle)
            is TaskListEvent.AddNewTask -> addNewTask(e)
        }
    }

    private fun changeTaskListTitle(id: Long, title: String) = ioLaunch {
        val e = taskListDao.getById(id)
            ?: throw IllegalArgumentException("can't find TaskListEntity with this id")
        taskListDao.update(e.copy(title = title))
    }

    private fun addNewTask(e: TaskListEvent.AddNewTask) = ioLaunch {
        taskDao.insert(
            TaskEntity(
                taskListId = e.id,
                projectId = projectDao.getSelectedProjectSync()!!.id,
                title = e.title,
                description = e.description,
                points = e.points
            )
        )
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

    private fun moveTask(e: TaskEvent.Move) = ioLaunch {
        val task =
            taskDao.getById(e.id) ?: throw IllegalArgumentException("can't find task with this id")
        taskDao.update(task.copy(taskListId = e.destTaskListId))
    }

    private fun changeTitle(e: TaskEvent.ChangeTitle) = ioLaunch {
        val task =
            taskDao.getById(e.id) ?: throw IllegalArgumentException("can't find task with this id")
        taskDao.update(task.copy(title = e.newTitle))
    }

    private fun changePoints(e: TaskEvent.ChangePoints) = ioLaunch {
        val task =
            taskDao.getById(e.id) ?: throw IllegalArgumentException("can't find task with this id")
        taskDao.update(task.copy(points = e.newPoints))
    }

    private fun deleteTask(e: TaskEvent.Delete) = ioLaunch {
        taskDao.deleteById(e.id)
    }

    private fun editTask(e: TaskEvent.Edit) = ioLaunch {
        val task =
            taskDao.getById(e.id) ?: throw IllegalArgumentException("can't find task with this id")
        taskDao.update(
            task.copy(
                title = e.newTitle,
                description = e.newDescription,
                points = e.newPoints
            )
        )
    }


    /**
     * functions to update flows
     */

    private fun ioLaunch(fcn: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = Dispatchers.IO, block = fcn)

    private fun taskEntityToState(e: TaskEntity): TaskState = TaskState(
        id = e.id,
        projectId = e.projectId,
        listId = e.taskListId,
        title = e.title,
        description = e.description,
        points = e.points
    )

    private fun taskListEntityToState(e: TaskListEntity): TaskListState = TaskListState(
        id = e.id,
        projectId = e.projectId,
        title = e.title
    )

    companion object {
        const val TAG = "TaskScreenVM"
    }
}