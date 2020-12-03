package id.sekdes.todoapps.views.contracts

import id.sekdes.todoapps.models.TodoModel

interface TodoAddContract {

    interface View{
        fun onSuccessInsertTodo(todoModel: TodoModel)

    }

    interface Presenter{
        fun insertTodo(todoModel: TodoModel)
    }

}