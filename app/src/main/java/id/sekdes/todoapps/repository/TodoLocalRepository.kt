package id.sekdes.todoapps.repository

import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.locale.entities.TodoEntity

interface TodoLocalRepository {
    fun getAllTodo(): List<TodoEntity>
    fun insertTodo(todoEntity: TodoEntity)
    fun updateTodo(todoEntity: TodoEntity)
    fun deleteTodo(todoEntity: TodoEntity)
}