package com.hfad.simplemanager

import android.app.Application
import androidx.room.Room
import com.hfad.simplemanager.dataBase.AppDatabase
import com.hfad.simplemanager.ui.ProjectsScreen.ProjectScreenVM
import com.hfad.simplemanager.ui.TaskListScreen.TaskScreenVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class SimpleManagerApp : Application() {
    private lateinit var db: AppDatabase
    private lateinit var repository: Repository

    lateinit var taskScreenVM: TaskScreenVM
    lateinit var projectScreenVM: ProjectScreenVM

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "simple_manager_db"
        ).fallbackToDestructiveMigration().build()

        repository = Repository(db)

        taskScreenVM = TaskScreenVM(db)
        projectScreenVM = ProjectScreenVM(db.projectDao())

//        runBlocking(Dispatchers.IO) {
//            db.projectDao().deleteAll()
//        }
    }
}