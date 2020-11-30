package id.sekdes.todoapps.repository.locale.daos

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import id.sekdes.todoapps.repository.locale.entities.TodoListPastEntity

interface TodoListPastDao {
    @Query("SELECT * FROM todo_list_past ORDER BY ID DESC")
    fun getAllTodo(): List<TodoListPastEntity>

    @Update
    fun updateTodo(entity: TodoListPastEntity)

    @Delete
    fun deleteTodo(entity: TodoListPastEntity)
}