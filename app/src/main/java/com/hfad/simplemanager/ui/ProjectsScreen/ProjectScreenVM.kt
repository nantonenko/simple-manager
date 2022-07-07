package com.hfad.simplemanager.ui.ProjectsScreen

import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.hfad.simplemanager.Repository
import com.hfad.simplemanager.dataBase.ProjectDao
import com.hfad.simplemanager.dataBase.ProjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectScreenVM(private val repo: Repository) : ViewModel() {

    val prjFlow: Flow<List<ProjectEntity>> get() = repo.projectFlow

    fun createNewProject() = viewModelScope.launch(Dispatchers.IO) {
        repo.projectDao.insert(ProjectEntity(name = "new project"))
    }

    fun updateProject(project: ProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        repo.projectDao.update(project)
    }

    fun onProjectSelected(project: ProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateCurrentProject(project)
    }
}