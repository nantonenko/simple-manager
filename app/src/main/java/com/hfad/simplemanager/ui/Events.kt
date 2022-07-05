package com.hfad.simplemanager.ui

sealed class Events

sealed class TaskEvents(val id: Int): Events() {
    class ChangeTitle(id: Int, val newName: String): TaskEvents(id)
    class ChangePoints(id: Int, newPoints: Int): TaskEvents(id)
    class Edit(id: Int, val newName: String, val newDescription: String, val newPoints: Int): TaskEvents(id)
    class Move(id: Int, val destTaskListId: Int): TaskEvents(id)
    class Delete(id: Int): TaskEvents(id)
}

sealed class TaskListEvents(val id: Int): Events() {
    class ChangeTitle(id: Int, val newTitle: String): TaskListEvents(id)
    class Delete(id: Int): TaskListEvents(id)
}