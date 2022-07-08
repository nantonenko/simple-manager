package com.hfad.simplemanager.ui.ProjectsScreen

import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.hfad.simplemanager.Repository
import com.hfad.simplemanager.dataBase.AppDatabase
import com.hfad.simplemanager.dataBase.ProjectDao
import com.hfad.simplemanager.dataBase.ProjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.nio.file.Files.delete

class ProjectScreenVM(private val projectDao: ProjectDao) : ViewModel() {

    val prjFlow: Flow<List<ProjectEntity>> get() = projectDao.getAll()

    fun createNewProject() = viewModelScope.launch(Dispatchers.IO) {
        projectDao.insert(ProjectEntity(name = "new project"))
    }

    fun updateProject(project: ProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        projectDao.update(project)
    }

    fun onProjectSelected(project: ProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        projectDao.getSelectedProjectSync()?.let { prj ->
            projectDao.update(prj.copy(isSelected = false))
        }
        projectDao.update(project.copy(isSelected = true))
    }

    fun onProjectDeleted(project: ProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        projectDao.delete(project)
    }
}