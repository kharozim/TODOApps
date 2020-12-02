package id.sekdes.todoapps.repository.locale

import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.entities.TodoEntity

class TodoLocalRepositoryImpl(private val dao: TodoDao) : TodoLocalRepository {
    override fun getAllTodo(): List<TodoEntity> = dao.getAllTodo()

    override fun insertTodo(todoEntity: TodoEntity) = dao.insertTodo(todoEntity)

    override fun updateTodo(todoEntity: TodoEntity) = dao.updateTodo(todoEntity)

    override fun deleteTodo(todoEntity: TodoEntity) = dao.deleteTodo(todoEntity)
}