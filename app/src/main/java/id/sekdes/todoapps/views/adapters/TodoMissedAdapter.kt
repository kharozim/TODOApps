package id.sekdes.todoapps.views.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import id.sekdes.todoapps.databinding.ItemListTodoBinding
import id.sekdes.todoapps.databinding.ItemListTodoHeaderBinding
import id.sekdes.todoapps.models.TodoModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class TodoMissedAdapter(
    private val context: Context, private val listener: TodoListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var todoList = mutableListOf<Todo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            TYPE_HEADER -> HeaderViewHolder(
                ItemListTodoHeaderBinding.inflate(LayoutInflater.from(context), parent, false)
            )
            TYPE_DATA -> MyViewHolder(
                ItemListTodoBinding.inflate(LayoutInflater.from(context), parent, false),
                listener
            )
            else -> throw IllegalArgumentException("Unsupported view type")
        }

    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_DATA = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(todoList[position]){
            is Todo.Category -> TYPE_HEADER
            is Todo.Data -> TYPE_DATA
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateTodo(it: List<TodoModel>){
        val list = mutableListOf<Todo>()
        val sortedList = it.sortedByDescending { it.dueDate }
        var temp = ""
        val dateNow = LocalDate.now()

        sortedList.forEach { model ->
            val date = model.dueDate
            if (date.isNotEmpty() && LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).isBefore(dateNow) && !model.isDone){
                if (temp != date) {
                    temp = date

                    list.add(Todo.Category(
                        LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).format(
                            DateTimeFormatter.ofLocalizedDate(
                            FormatStyle.MEDIUM))
                    ))
                }
                list.add(Todo.Data(model))
            }
            this.setData(list)
        }
    }

    private fun setData(item: MutableList<Todo>){
        this.todoList = item
        notifyDataSetChanged()
    }

    fun deleteTodo(position: Int) {
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }


    fun getData(position: Int): Todo{
        return todoList[position]
    }

    fun addTodo(todoModel: Todo) {
        todoList.add(0, todoModel)
        notifyItemInserted(0)
    }

    interface TodoListener {
        fun onClick(todo: TodoModel)
        fun onDelete(todo: TodoModel, position: Int)
        fun onLongPress(todo: TodoModel, position: Int)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val todo = todoList[position]

        if (todo is Todo.Data && holder is MyViewHolder) {
            holder.itemBinding.todo = todo.todo
            holder.itemBinding.apply {
                ivImage.visibility = if (todo.todo.images.isNullOrEmpty()) View.GONE else View.VISIBLE
                ivVoiceNote.visibility = if (todo.todo.voiceNote.isEmpty()) View.GONE else View.VISIBLE

            }
        } else if (todo is Todo.Category && holder is HeaderViewHolder) {
            holder.bindData(todo.date)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    inner class HeaderViewHolder(
        private val binding: ItemListTodoHeaderBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bindData(date: String){
            binding.run {
                tvDateHeader.text = date.toUpperCase(Locale.getDefault())
            }

        }
    }

    class MyViewHolder(
        val itemBinding: ItemListTodoBinding,
        private val listener: TodoListener
    ) : RecyclerView.ViewHolder(itemBinding.root){

        private var binding : ItemListTodoBinding? = null

        init {
            this.binding = itemBinding

        }
    }

}