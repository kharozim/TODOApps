package id.sekdes.todoapps.repository.locale

import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.daos.TodoListDao
import id.sekdes.todoapps.repository.locale.entities.TodoListEntity

class TodoLocalRepositoryImpl(private val dao: TodoListDao) : TodoLocalRepository {
    override fun getAllTodo(): List<TodoListEntity> = dao.getAllTodo()

    override fun insertTodo(todoListEntity: TodoListEntity) = dao.insertTodo(todoListEntity)

    override fun updateTodo(todoListEntity: TodoListEntity) = dao.updateTodo(todoListEntity)

    override fun deleteTodo(todoListEntity: TodoListEntity) = dao.deleteTodo(todoListEntity)
}