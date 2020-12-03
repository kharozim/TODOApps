package id.sekdes.todoapps.presenter

import id.sekdes.todoapps.views.contracts.TodoAddContract
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.TodoLocalRepository
import java.util.concurrent.Executors

class TodoAddPresenter (
    private val view: TodoAddContract.View,
    private val repository: TodoLocalRepository
) : TodoAddContract.Presenter{

    private val executor by lazy { Executors.newFixedThreadPool(4) }

    override fun insertTodo(todoModel: TodoModel) {
        executor.execute {
            val todo = repository.insertTodo(todoModel)
            view.onSuccessInsertTodo(todo)
        }

    }

}