package id.sekdes.todoapps.repository

import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.locale.entities.TodoEntity

interface TodoLocalRepository {
    fun getAllTodo(): List<TodoModel>
    fun insertTodo(todo: TodoModel) : TodoModel
    fun updateTodo(todo: TodoModel) : TodoModel
    fun deleteTodo(todo: TodoModel) : Long
}