package com.hfad.simplemanager

import com.hfad.simplemanager.dataBase.AppDatabase
import com.hfad.simplemanager.dataBase.ProjectEntity
import com.hfad.simplemanager.dataBase.TaskEntity
import com.hfad.simplemanager.dataBase.TaskListEntity
import kotlinx.coroutines.flow.Flow

class Repository(private val db: AppDatabase) {

    val projectDao = db.projectDao()
    private val taskListDao = db.taskListDao()
    private val taskDao = db.taskDao()

    val projectFlow get() = projectDao.getAll()

    suspend fun updateCurrentProject(project: ProjectEntity) {
        projectDao.getSelectedProjectSync()?.let { prj ->
            projectDao.update(prj.copy(isSelected = false))
        }
        projectDao.update(project.copy(isSelected = true))
    }

    suspend fun getCurrentProjectId(): Long? = projectDao.getSelectedProjectSync()?.id


    // Task list methods

    suspend fun getTaskListsFlow(projectId: Long): Flow<List<TaskListEntity>> =
        taskListDao.getAllForProject(projectId)

    suspend fun updateTaskList(listEntity: TaskListEntity) = taskListDao.update(listEntity)

    suspend fun insertTaskList(listEntity: TaskListEntity) = taskListDao.insert(listEntity)

    suspend fun deleteTaskList(listEntity: TaskListEntity) = taskListDao.delete(listEntity)


    // Tasks methods

    suspend fun getTasksFlow(projectId: Long): Flow<List<TaskEntity>> =
        taskDao.getAllForProject(projectId)

    suspend fun updateTask(taskEntity: TaskEntity) = taskDao.update(taskEntity)

    suspend fun insertTask(taskEntity: TaskEntity) = taskDao.insert(taskEntity)

    suspend fun deleteTask(taskEntity: TaskEntity) = taskDao.delete(taskEntity)
}