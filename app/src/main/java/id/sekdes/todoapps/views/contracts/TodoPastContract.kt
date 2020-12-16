package id.sekdes.todoapps.views.contracts

import id.sekdes.todoapps.models.TodoModel

interface TodoPastContract {

    // all methods to do something on views
    interface View{
        fun loading(state: Boolean)
        fun onSuccessGetAllTodo(list: List<TodoModel>)
        fun onEmptyTodo(state: Boolean)
        fun onSuccessUpdateTodo(todoModel: TodoModel)

    }

    // all methods to do something with data
    interface Presenter{
        fun getAllTodo(): List<TodoModel>
        fun getEditTodo(todoModel: TodoModel)

    }

}