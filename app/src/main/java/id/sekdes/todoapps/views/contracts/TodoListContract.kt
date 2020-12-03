package id.sekdes.todoapps.views.contracts

import id.sekdes.todoapps.models.TodoModel

// rules
interface TodoListContract {

    // all methods to do something on views
    interface View{
        fun loading(state: Boolean)
        fun onSuccessGetAllTodo(list: List<TodoModel>)
        fun onSuccessUpdateTodo(todoModel: TodoModel)
        fun onEmptyTodo(state: Boolean)
    }

    // all methods to do something with data
    interface Presenter{
        fun getAllTodo(): List<TodoModel>
    }

}