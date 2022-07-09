package com.hfad.simplemanager.ui

sealed class Event

sealed class TaskScreenEvent : Event() {
    class NewTaskList(val title: String) : TaskScreenEvent()
}

sealed class TaskEvent(val id: Long) : Event() {
    class ChangeTitle(id: Long, val newTitle: String) : TaskEvent(id)
    class ChangePoints(id: Long, val newPoints: Int) : TaskEvent(id)
    class Edit(id: Long, val newTitle: String, val newDescription: String, val newPoints: Int) :
        TaskEvent(id)

    class Move(id: Long, val destTaskListId: Long) : TaskEvent(id)
    class MoveWithArrows(id: Long, val diraction: Diractions ): TaskEvent(id) {
        enum class Diractions { UP, DOWN, LEFT, RIGHT }
    }
    class Delete(id: Long) : TaskEvent(id)
}

sealed class TaskListEvent(val id: Long) : Event() {
    class Move(id: Long, val diraction: Diractions): TaskListEvent(id) {
        enum class Diractions { LEFT, RIGHT }
    }
    class ChangeTitle(id: Long, val newTitle: String) : TaskListEvent(id)
    class Delete(id: Long) : TaskListEvent(id)
    class AddNewTask(id: Long, val title: String, val description: String, val points: Int) :
        TaskListEvent(id)
}