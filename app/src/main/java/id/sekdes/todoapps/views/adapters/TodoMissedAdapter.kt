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
import id.sekdes.todoapps.views.util.DateUtil
import java.text.SimpleDateFormat
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

    fun generateTodo(it: List<TodoModel>){
        val list = mutableListOf<Todo>()
        val sortedList = it.sortedByDescending { it.dueDate }
        var temp = ""
        val dateNow = Calendar.getInstance().time

        val mFormatDate = SimpleDateFormat(DateUtil.dateFormat, Locale.getDefault())


        sortedList.forEach { model ->
            val date = model.dueDate
            if (date.isNotEmpty() &&  mFormatDate.parse(date)?.before(mFormatDate.parse(mFormatDate.format(dateNow))) == true && !model.isDone){
                if (temp != date) {
                    temp = date

                    list.add(Todo.Category( SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(mFormatDate.parse(date)?:dateNow)
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

    fun deleteData(todo: TodoModel) {
        val model = Todo.Data(todo)
        val index = todoList.indexOfFirst { it == model }
        if (index != -1) {
            todoList.removeAt(index)

            notifyItemRemoved(index)
        }

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
        fun onDone(todo: TodoModel)
        fun onLongPress(todo: TodoModel, position: Int)
    }

    fun updateData(todoModel: TodoModel) {
        val model = Todo.Data(todoModel)
        val index = todoList.indexOfFirst { it == model }
        if (index != -1) {
            todoList[index] = model
            notifyItemChanged(index)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val todo = todoList[position]

        if (todo is Todo.Data && holder is MyViewHolder) {
            holder.itemBinding.todo = todo.todo
            holder.itemBinding.apply {
                ivImage.visibility = if (todo.todo.images.isNullOrEmpty()) View.GONE else View.VISIBLE
                ivVoiceNote.visibility = if (todo.todo.voiceNote.isEmpty()) View.GONE else View.VISIBLE
                root.setOnClickListener {
                    listener.onClick(todo.todo)
                }
                root.setOnLongClickListener {
                    listener.onLongPress(todo.todo, position)
                    return@setOnLongClickListener true
                }
                cbDone.setOnClickListener {
                    todo.todo.isDone = !todo.todo.isDone

                    listener.onDone(todo.todo)
                }
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
                tvDateHeader.text = date
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