package com.hfad.simplemanager

import android.app.Application
import androidx.room.Room
import com.hfad.simplemanager.dataBase.AppDatabase
import com.hfad.simplemanager.ui.ProjectsScreen.ProjectScreenVM
import com.hfad.simplemanager.ui.TaskListScreen.TaskScreenVM



class SimpleManagerApp : Application() {
    private lateinit var db: AppDatabase
    lateinit var taskScreenVM: TaskScreenVM
    lateinit var projectScreenVM: ProjectScreenVM

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "simple_manager_db"
        ).build()

        taskScreenVM = TaskScreenVM()
        projectScreenVM = ProjectScreenVM()

        taskScreenVM.taskDao = db.taskDao()
        taskScreenVM.taskListDao = db.taskListDao()
        projectScreenVM.prjDao = db.projectDao()
    }
}