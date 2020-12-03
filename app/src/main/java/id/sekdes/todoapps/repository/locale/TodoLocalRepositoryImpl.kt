package id.sekdes.todoapps.repository.locale

import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.models.toEntity
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.entities.toModel

class TodoLocalRepositoryImpl(private val dao: TodoDao) : TodoLocalRepository {
    override fun getAllTodo(): List<TodoModel> = dao.getAllTodo().asSequence().map { it.toModel() }.toList()

    override fun insertTodo(todo: TodoModel) : TodoModel {
        val todoEntity = todo.toEntity()
        val id = dao.insertTodo(todoEntity)
        return todo.copy(id = id)
    }

    override fun updateTodo(todo: TodoModel): TodoModel {
        println("updateingggggg")
        dao.updateTodo(todo.toEntity())
        return todo
    }

    override fun deleteTodo(todo: TodoModel): Long {
        dao.deleteTodo(todo.toEntity())
        return todo.id
    }
}