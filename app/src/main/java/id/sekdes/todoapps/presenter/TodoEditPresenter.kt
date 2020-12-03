package id.sekdes.todoapps.presenter

import id.sekdes.todoapps.contract.TodoEditContract
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.TodoLocalRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TodoEditPresenter(
    private val view: TodoEditContract.View,
    private val repository: TodoLocalRepository
) : TodoEditContract.Presenter {

    private val executor by lazy { Executors.newFixedThreadPool(4) }

    override fun getEditTodo(todoModel: TodoModel) {
        executor.execute {
            val todo = repository.updateTodo(todoModel)
            println(todo)
            view.onSuccessEditTodo(todo)
        }
    }
}