package id.sekdes.todoapps.repository.locale.daos

import androidx.room.*
import id.sekdes.todoapps.repository.locale.entities.TodoListEntity

@Dao
interface TodoListDao {
    @Query("SELECT * FROM todo_list ORDER BY ID DESC")
    fun getAllTodo(): List<TodoListEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTodo(entity: TodoListEntity)

    @Update
    fun updateTodo(entity: TodoListEntity)

    @Delete
    fun deleteTodo(entity: TodoListEntity)
}