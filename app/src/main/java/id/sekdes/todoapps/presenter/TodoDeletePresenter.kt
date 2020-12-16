package id.sekdes.todoapps.presenter

import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.views.contracts.TodoDeleteContract
import java.util.concurrent.Executors

class TodoDeletePresenter(
    private val view : TodoDeleteContract.View,
    private val repository : TodoLocalRepository
) : TodoDeleteContract.Presenter  {

    private val executor by lazy { Executors.newFixedThreadPool(4) }

    override fun getDeleteTodo(todoModel: TodoModel) {
        executor.execute {
            val id = repository.deleteTodo(todoModel)
            view.onSuccessDeleteTodo(id)
        }
    }
}