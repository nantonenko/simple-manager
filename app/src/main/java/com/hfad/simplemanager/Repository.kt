package com.hfad.simplemanager

import com.hfad.simplemanager.dataBase.AppDatabase
import com.hfad.simplemanager.dataBase.ProjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Repository(private val db: AppDatabase) {

    val projectDao = db.projectDao()
    private val taskListDao = db.taskListDao()
    private val taskDao = db.taskDao()

    val projectFlow get() = projectDao.getAll()
    val taskListFlow get() = taskListDao.getAll()
    val taskFlow get() = taskDao.getAll()

    suspend fun updateCurrentProject(project: ProjectEntity) {
        projectDao.getSelectedProject()?.let { prj ->
            projectDao.update(prj.copy(isSelected = false))
        }
        projectDao.update(project.copy(isSelected = true))
    }
}