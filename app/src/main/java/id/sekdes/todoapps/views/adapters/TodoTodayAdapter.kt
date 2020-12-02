package id.sekdes.todoapps.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.ItemListTodoBinding
import id.sekdes.todoapps.models.TodoModel

class TodoTodayAdapter (
    private val context: Context,
    private val listener: TodoListener
): RecyclerView.Adapter<TodoTodayAdapter.MyViewHolder>(){

    private var todoList = mutableListOf<TodoModel>()


    class MyViewHolder(
        val itemListTodoBinding: ItemListTodoBinding,
        private val listener: TodoListener
    ) : RecyclerView.ViewHolder(itemListTodoBinding.root){
        private var binding : ItemListTodoBinding? = null

        init {
            this.binding = itemListTodoBinding
        }
    }

    fun setData(item: MutableList<TodoModel>){
        this.todoList = item
        notifyDataSetChanged()
    }

    interface TodoListener{
        fun onClick(todo: TodoModel)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemListTodoBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_todo, parent, false)

        return MyViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemListTodoBinding.todo = todoList[position]
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}