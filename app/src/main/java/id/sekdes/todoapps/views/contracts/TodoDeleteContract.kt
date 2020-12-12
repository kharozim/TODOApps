package id.sekdes.todoapps.views.contracts

import id.sekdes.todoapps.models.TodoModel

interface TodoDeleteContract {

    interface View{
        fun loading(isLoading : Boolean)
        fun onSuccessDeleteTodo(todo: TodoModel)
    }

    interface Presenter{
        fun getDeleteTodo(todoModel: TodoModel)
    }
}