package id.sekdes.todoapps.contract

import id.sekdes.todoapps.models.TodoModel

interface TodoAddContract {

    interface View{
        fun onSuccessInsertTodo(todoModel: TodoModel)

    }

    interface Presenter{
        fun insertTodo(todoModel: TodoModel)
    }

}