package com.hfad.simplemanager

import android.app.Application
import androidx.room.Room
import com.hfad.simplemanager.dataBase.AppDatabase
import com.hfad.simplemanager.ui.taskListScreen.TaskScreenVM

class SimpleManagerApp : Application() {
    private lateinit var db: AppDatabase
    private lateinit var repository: Repository
    private lateinit var taskScreenVM: TaskScreenVM

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "simple_manager_db"
        ).build()

        repository = Repository(db)
        taskScreenVM = TaskScreenVM()
        taskScreenVM.repo = repository
    }
}