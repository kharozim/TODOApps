package id.sekdes.todoapps.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.ItemListTodoBinding
import id.sekdes.todoapps.models.TodoModel

class TodoTodayAdapter(
    private val context: Context,
    private val listener: TodoListener
) : RecyclerView.Adapter<TodoTodayAdapter.MyViewHolder>() {

    private var todoList = mutableListOf<TodoModel>()


    class MyViewHolder(
        val itemListTodoBinding: ItemListTodoBinding,
        private val listener: TodoListener
    ) : RecyclerView.ViewHolder(itemListTodoBinding.root) {
        private var binding: ItemListTodoBinding? = null

        init {
            this.binding = itemListTodoBinding

        }
    }

    fun updateData(todoModel: TodoModel) {
        val index = todoList.indexOfFirst { it.id == todoModel.id }
        if (index != -1) {
            todoList[index] = todoModel
            notifyItemChanged(index)
        }
    }

    fun setData(item: MutableList<TodoModel>) {
        this.todoList = item
        notifyDataSetChanged()
    }

    interface TodoListener {
        fun onClick(todo: TodoModel)
        fun onDone(todo: TodoModel)
        fun onDelete(todo: TodoModel)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemListTodoBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_todo, parent, false)

        return MyViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = todoList[position]
        holder.itemListTodoBinding.todo = model
        holder.itemListTodoBinding.apply {
            root.setOnClickListener {
                listener.onClick(todo!!)
            }
            cbDone.setOnClickListener {
                todo!!.isDone = !todo!!.isDone

                listener.onDone(todo!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun deleteData(id: Long) {
        val indext = todoList.indexOfFirst { it.id == id }
        if (indext != -1) {
            todoList.removeAt(indext)
            notifyItemRemoved(indext)
        }
    }
}