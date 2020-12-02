package id.sekdes.todoapps.contract

import id.sekdes.todoapps.models.TodoModel

// rules
interface TodoListContract {

    // all methods to do something on views
    interface View{
        fun loading(state: Boolean)
        fun onSuccessGetAllTodo(list: List<TodoModel>)
        fun onEmptyTodo(state: Boolean)
    }

    // all methods to do something with data
    interface Presenter{
        fun getAllTodo(): List<TodoModel>
    }

}