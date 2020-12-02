package id.sekdes.todoapps.presenter

import id.sekdes.todoapps.contract.TodoListContract
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.entities.toModel
import java.util.concurrent.Executors

class TodoListPresenter(
    // get view
    private val view: TodoListContract.View,
    // get repository
    private val repository: TodoLocalRepository
): TodoListContract.Presenter {

    // must use this to get room
    private val executor by lazy { Executors.newFixedThreadPool(4) }

    // get all data from room
    override fun getAllTodo( ): List<TodoModel> {
        // declare
        val todoList = mutableListOf<TodoModel>()
        // access room db
        executor.execute{
            view.loading(true)
            // convert List<Entity> to List<Model>
            val todoListModel = repository.getAllTodo().asSequence().map { it.toModel() }.toList()
            todoList.addAll(todoListModel)
            view.loading(false)
            if (todoList.isNullOrEmpty()){
                view.onEmptyTodo(true)
            }else{
                view.onEmptyTodo(false)
                view.onSuccessGetAllTodo(todoList)
            }
        }
        return todoList
    }
}