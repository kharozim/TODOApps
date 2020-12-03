package id.sekdes.todoapps.views.contracts

import id.sekdes.todoapps.models.TodoModel

interface TodoEditContract {

    // all methods to do something on views
    interface View{
        fun loading(state: Boolean)
        fun onSuccessEditTodo(todoModel: TodoModel)
        fun onEmptyTodo(state: Boolean)
    }

    // all methods to do something with data
    interface Presenter{
        fun getEditTodo(todoModel: TodoModel)
    }
}