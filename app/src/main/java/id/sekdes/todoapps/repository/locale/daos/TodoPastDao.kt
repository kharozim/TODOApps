package id.sekdes.todoapps.repository.locale.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import id.sekdes.todoapps.repository.locale.entities.TodoPastEntity

@Dao
interface TodoPastDao {
    @Query("SELECT * FROM todo_past_table ORDER BY ID DESC")
    fun getAllTodo(): List<TodoPastEntity>

    @Update
    fun updateTodo(entity: TodoPastEntity)

    @Delete
    fun deleteTodo(entity: TodoPastEntity)
}