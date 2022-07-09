package com.hfad.simplemanager.dataBase

import android.os.FileObserver.DELETE
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insert(task: TaskEntity): Long

    @Delete
    fun delete(task: TaskEntity)

    @Query("DELETE FROM tasks")
    fun deleteAll()

    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteById(id: Long)

    @Update
    fun update(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY position")
    fun getAll(): Flow<List<TaskEntity>>


    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks ORDER BY position")
    fun getAllSync(): List<TaskEntity>

    @Query("SELECT task_list_id FROM tasks WHERE id = :taskId")
    fun getListIdForTask(taskId: Long): Long

    @Query("SELECT * FROM tasks WHERE task_list_id = :taskListId ORDER BY position")
    fun getAllForListSync(taskListId: Long): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE project_id = :projectId ORDER BY position")
    fun getAllForProject(projectId: Long): Flow<List<TaskEntity>>
}

@Dao
interface TaskListDao {
    @Insert
    fun insert(list: TaskListEntity): Long

    @Delete
    fun delete(list: TaskListEntity)

    @Query("DELETE FROM task_lists")
    fun deleteAll()

    @Query("DELETE FROM task_lists WHERE id = :id")
    fun deleteById(id: Long)

    @Update
    fun update(list: TaskListEntity)

    @Query("SELECT * FROM task_lists  ORDER BY position")
    fun getAll(): Flow<List<TaskListEntity>>

    @Query("SELECT * FROM task_lists WHERE id = :id")
    fun getById(id: Long): TaskListEntity?

    @Query("SELECT * FROM task_lists ORDER BY position")
    fun getAllSync(): List<TaskListEntity>

    @Query("SELECT * FROM task_lists WHERE project_id = :projectId ORDER BY position")
    fun getAllForProject(projectId: Long): Flow<List<TaskListEntity>>

    @Query("SELECT * FROM task_lists WHERE project_id = :projectId ORDER BY position")
    fun getAllForProjectSync(projectId: Long): List<TaskListEntity>
}

@Dao
interface ProjectDao {
    @Insert
    fun insert(project: ProjectEntity): Long

    @Delete
    fun delete(project: ProjectEntity)

    @Query("DELETE FROM projects")
    fun deleteAll()

    @Query("DELETE FROM projects WHERE id = :id")
    fun deleteById(id: Long)

    @Update
    fun update(project: ProjectEntity)

    @Query("SELECT * FROM projects")
    fun getAll(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects")
    fun getAllSync(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE is_selected = 1")
    fun getSelectedProjectSync(): ProjectEntity?

    @Query("SELECT * FROM projects WHERE is_selected = 1")
    fun getSelectedProject(): Flow<ProjectEntity?>
}