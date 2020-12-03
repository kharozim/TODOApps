package id.sekdes.todoapps.repository.locale.daos

import androidx.room.*
import id.sekdes.todoapps.repository.locale.entities.TodoEntity

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY ID DESC")
    fun getAllTodo(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTodo(entity: TodoEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTodo(entity: TodoEntity)

    @Delete
    fun deleteTodo(entity: TodoEntity)
}