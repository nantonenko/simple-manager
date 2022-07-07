package com.hfad.simplemanager

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hfad.simplemanager.dataBase.*
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RoomDbTest {

    private lateinit var db: AppDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var taskListDao: TaskListDao
    private lateinit var projectDao: ProjectDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        taskDao = db.taskDao()
        taskListDao = db.taskListDao()
        projectDao = db.projectDao()
    }

    @Test
    fun onStartProjectsMustBeEmpty() {
        val p = projectDao.getAllSync()
        assertTrue(p.isEmpty())
    }

    @Test
    fun onStartTaskListMustBeEmpty() {
        val p = taskListDao.getAllSync()
        assertTrue(p.isEmpty())
    }

    @Test
    fun onStartTasksMustBeEmpty() {
        val p = taskDao.getAllSync()
        assertTrue(p.isEmpty())
    }

    @Test
    fun insertProject() {
        val id = baseProjectInsert()
        println("id = $id")
        val r = projectDao.getAllSync()
        assertTrue(r.size == 1)
        assertEquals(basePrj.name, r[0].name)
        assertEquals(basePrj.description, r[0].description)
    }

    @Test
    fun insertTaskList() {
        val tlid = baseListInsertWithProject()
        val r = taskListDao.getAllSync()
        println("id = $tlid")
        assertTrue(r.size == 1)
        assertEquals(baseList.title, r[0].title)
    }

    @Test
    fun insertTask() {
        val pid = baseProjectInsert()
        val tlid = baseListInsert(pid)

        val t = TaskEntity(
            title = "task 1",
            description = "description 1",
            points = 10,
            projectId = pid,
            taskListId = tlid
        )
        taskDao.insert(t)
        val r = taskDao.getAllSync()
        assertTrue(r.size == 1)
        assertEquals(t.title, r[0].title)
        assertEquals(t.description, r[0].description)
        assertEquals(t.points, r[0].points)
    }

    @Test
    fun cascadeTasksDeletion() {
        val pid = baseProjectInsert()
        val tlid = baseListInsert(pid)

        repeat(10) {
            val t = TaskEntity(points = it, projectId = pid, taskListId = tlid)
            taskDao.insert(t)
        }
        taskListDao.deleteById(tlid.toLong())
        val r = taskDao.getAllSync()
        assertTrue(r.isEmpty())
    }

    @Test
    fun cascadeProjectDeletion() {
        val pid = baseProjectInsert()
        val tlid = baseListInsert(pid)

        repeat(10) {
            val t = TaskEntity(points = it, projectId = pid, taskListId = tlid)
            taskDao.insert(t)
        }

        projectDao.deleteById(pid.toLong())

        assertTrue(taskListDao.getAllSync().isEmpty())
        assertTrue(taskDao.getAllSync().isEmpty())
    }

    @Test
    fun getSelectedProject() {
        projectDao.insert(basePrj.copy(isSelected = true))
        val p = projectDao.getSelectedProject()
        println(p)
    }

    @Test
    fun getSelectedProjectWhenNoSelectedProject() {
        val p = projectDao.getSelectedProject()
        println(p)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        taskDao.deleteAll()
        taskListDao.deleteAll()
        projectDao.deleteAll()
        db.close()
    }


    private val basePrj = ProjectEntity(name = "base", description = "none")
    private fun baseProjectInsert(): Long {
        return projectDao.insert(basePrj)
    }

    private val baseList = TaskListEntity(title = "base")

    private fun baseListInsert(projectId: Long): Long {
        val pid = baseProjectInsert()
        return taskListDao.insert(baseList.copy(projectId = projectId))
    }

    private fun baseListInsertWithProject(): Long {
        val pid = baseProjectInsert()
        return taskListDao.insert(baseList.copy(projectId = pid))
    }

}