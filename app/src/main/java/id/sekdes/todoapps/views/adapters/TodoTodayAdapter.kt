package id.sekdes.todoapps.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sekdes.todoapps.databinding.ItemListTodoBinding
import id.sekdes.todoapps.databinding.ItemListTodoHeaderBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.views.util.DateUtil
import java.text.SimpleDateFormat
import java.util.*

class TodoTodayAdapter(
    private val context: Context,
    private val listener: TodoListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var todoList = mutableListOf<Todo>()
    private var listOngoing = mutableListOf<Todo>()
    private var listCompleted = mutableListOf<Todo>()

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_DATA = 1
    }


    class MyViewHolder(
        val itemListTodoBinding: ItemListTodoBinding,
        private val listener: TodoListener
    ) : RecyclerView.ViewHolder(itemListTodoBinding.root) {
        private var binding: ItemListTodoBinding? = null

        init {
            this.binding = itemListTodoBinding
        }
    }

    fun getAllCompleted(): Int{
        return listCompleted.size
    }
    fun getAllOngoing(): Int{
        return listOngoing.size
    }


    fun updateData(todoModel: TodoModel) {
        val model = Todo.Data(todoModel)
        val index = todoList.indexOfFirst { it == model }
        if (index != -1) {
            todoList[index] = model
            notifyItemChanged(index)
        }
    }

    private fun setData(item: MutableList<Todo>) {
        this.todoList = item
        notifyDataSetChanged()
    }

    interface TodoListener {
        fun onClick(todo: TodoModel)
        fun onDone(todo: TodoModel)
        fun onDelete(todo: TodoModel)
    }
    fun generateTodo(it: List<TodoModel>){

        val list = mutableListOf<Todo>()
         val listOngoing = mutableListOf<Todo>()
         val listCompleted = mutableListOf<Todo>()
        val sortedList = it.sortedByDescending { it.dueDate }
        var temp = ""
        val dateNow = Calendar.getInstance().time

        val mFormatDate = SimpleDateFormat(DateUtil.dateFormat, Locale.getDefault())

        sortedList.forEach { model ->
            val date = model.dueDate

            if (date.isNotEmpty() && mFormatDate.parse(date)?.equals(mFormatDate.parse(mFormatDate.format(dateNow))) == true ){
                if (model.isDone){
                    listCompleted.add(Todo.Data(model))
                }else{
                    listOngoing.add(Todo.Data(model))
                }
            }

        }
        if (listOngoing.isNotEmpty()){
            this.listOngoing.clear()
            list.add(Todo.Category("Ongoing"))
            list.addAll(listOngoing)
        }
        this.listOngoing = listOngoing

        if (listCompleted.isNotEmpty()){
            this.listCompleted.clear()
            list.add(Todo.Category("Completed"))
            list.addAll(listCompleted)
        }
        this.listCompleted = listCompleted

        this.setData(list)
    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val todo = todoList[position]

        if (todo is Todo.Data && holder is MyViewHolder) {
            holder.itemListTodoBinding.todo = todo.todo
            holder.itemListTodoBinding.apply {
                ivImage.visibility = if (todo.todo.images.isNullOrEmpty()) View.GONE else View.VISIBLE
                ivVoiceNote.visibility = if (todo.todo.voiceNote.isEmpty()) View.GONE else View.VISIBLE
                root.setOnClickListener {
                    listener.onClick(todo.todo)
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

    override fun getItemViewType(position: Int): Int {
        return when(todoList[position]){
            is Todo.Category -> TYPE_HEADER
            is Todo.Data -> TYPE_DATA
        }
    }

    fun deleteData(position: Int) {
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    class HeaderViewHolder(
        private val binding: ItemListTodoHeaderBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bindData(date: String){
            binding.run {
                tvDateHeader.text = date
            }

        }
    }
}