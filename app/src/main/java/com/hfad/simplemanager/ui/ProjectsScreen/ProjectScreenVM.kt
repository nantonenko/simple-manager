package com.hfad.simplemanager.ui.ProjectsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.hfad.simplemanager.dataBase.ProjectDao
import com.hfad.simplemanager.dataBase.ProjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProjectScreenVM : ViewModel() {

    lateinit var prjDao: ProjectDao
    val prjFlow: Flow<List<ProjectEntity>> get() = prjDao.getAll()

    fun createNewProject() = viewModelScope.launch(Dispatchers.IO) {
        prjDao.insert(ProjectEntity(name = "new project"))
    }

    fun updateProject(e: ProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        prjDao.update(e)
    }
}