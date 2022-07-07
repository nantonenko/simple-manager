package com.hfad.simplemanager.ui

sealed class Event

sealed class TaskScreenEvent : Event() {
    class NewTaskList(val title: String) : TaskScreenEvent()
}

sealed class TaskEvent(val id: Long) : Event() {
    class ChangeTitle(id: Long, val newName: String) : TaskEvent(id)
    class ChangePoints(id: Long, val newPoints: Int) : TaskEvent(id)
    class Edit(id: Long, val newName: String, val newDescription: String, val newPoints: Int) :
        TaskEvent(id)

    class Move(id: Long, val destTaskListId: Long) : TaskEvent(id)
    class Delete(id: Long) : TaskEvent(id)
}

sealed class TaskListEvent(val id: Long) : Event() {
    class ChangeTitle(id: Long, val newTitle: String) : TaskListEvent(id)
    class Delete(id: Long) : TaskListEvent(id)
    class AddNewTask(id: Long, val title: String, val description: String, val points: Int) :
        TaskListEvent(id)
}