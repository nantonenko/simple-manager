package com.hfad.simplemanager.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskListEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_list_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "task_list_id", index = true) val taskListId: Long = 0,
    @ColumnInfo(name = "project_id", index = true) val projectId: Long = 0,
    @ColumnInfo val title: String = "",
    @ColumnInfo val description: String = "",
    @ColumnInfo val points: Int = 0
)

@Entity(
    tableName = "task_lists",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "project_id") val projectId: Long = 0,
    @ColumnInfo val title: String = ""
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val name: String = "",
    @ColumnInfo val description: String = "",
    @ColumnInfo(name = "is_selected") val isSelected: Boolean = false
)
