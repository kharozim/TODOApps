package id.sekdes.todoapps.repository

import id.sekdes.todoapps.repository.locale.entities.TodoListEntity

interface TodoLocalRepository {
    fun getAllTodo(): List<TodoListEntity>
    fun insertTodo(todoListEntity: TodoListEntity)
    fun updateTodo(todoListEntity: TodoListEntity)
    fun deleteTodo(todoListEntity: TodoListEntity)
}