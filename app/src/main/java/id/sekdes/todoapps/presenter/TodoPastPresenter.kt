package id.sekdes.todoapps.presenter

import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.views.contracts.TodoPastContract
import java.util.concurrent.Executors

class TodoPastPresenter(
private val view: TodoPastContract.View,
private val repository: TodoLocalRepository
): TodoPastContract.Presenter {

    private val executor by lazy { Executors.newFixedThreadPool(4) }

    override fun getAllTodo( ): List<TodoModel> {
        val todoList = mutableListOf<TodoModel>()
        executor.execute{
            val todoListModel = repository.getAllTodo()
            todoList.addAll(todoListModel)
            if (todoList.isNullOrEmpty()){
                view.onEmptyTodo(true)
            }else{
                view.onEmptyTodo(false)
                view.onSuccessGetAllTodo(todoList)
            }
        }
        return todoList
    }
    override fun getEditTodo(todoModel: TodoModel) {
        executor.execute {
            val todo = repository.updateTodo(todoModel)
            view.onSuccessUpdateTodo(todo)
        }
    }

}