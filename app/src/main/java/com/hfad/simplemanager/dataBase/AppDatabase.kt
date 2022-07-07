package com.hfad.simplemanager.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class, TaskListEntity::class, ProjectEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskListDao(): TaskListDao
    abstract fun projectDao(): ProjectDao
}