package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.sekdes.todoapps.views.contracts.TodoListContract
import id.sekdes.todoapps.databinding.FragmentListBinding
import id.sekdes.todoapps.databinding.TodoMenuDialogBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoDeletePresenter
import id.sekdes.todoapps.presenter.TodoListPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.services.AlarmRemainderService.Companion.cancelAlarmReminder
import id.sekdes.todoapps.views.adapters.TodoTodayAdapter
import id.sekdes.todoapps.views.contracts.TodoDeleteContract

class ListFragment :
    Fragment(),
    TodoListContract.View,
    TodoDeleteContract.View,
    TodoTodayAdapter.TodoListener {

    lateinit var binding: FragmentListBinding
    private val adapter by lazy { TodoTodayAdapter(requireContext(), this) }
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoListContract.Presenter by lazy {TodoListPresenter(this,repository)}
    private val presenterDel : TodoDeleteContract.Presenter by lazy { TodoDeletePresenter(this,repository) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater, container, false)

        setView()
        return binding.root
    }

    private fun setView() {
        binding.run {
            rvList.adapter = adapter
        }
    }

    override fun loading(state: Boolean) {
        requireActivity().runOnUiThread {
            binding.run {
                pbList.visibility = if (state) View.VISIBLE else View.GONE
                rvList.visibility = if (state) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onSuccessDeleteTodo(todo: TodoModel) {
        requireActivity().runOnUiThread {
            binding.run {
                adapter.deleteData(todo)
            }
            presenter.getAllTodo()
        }
    }

    override fun onSuccessGetAllTodo(list: List<TodoModel>) {
        requireActivity().runOnUiThread {

            adapter.generateTodo(list.toMutableList())

            binding.apply {
                val x = adapter.getAllCompleted()
                val max = adapter.getAllCompleted() + adapter.getAllOngoing()
                progressBar.max = max
                progressBar.progress = x
                tvProgress.text = "$x / $max"
            }
        }
    }

    override fun onSuccessUpdateTodo(todoModel: TodoModel) {
        requireActivity().runOnUiThread {
            adapter.updateData(todoModel)
            Toast.makeText(requireContext(), "${todoModel.id} berhasil diubah", Toast.LENGTH_SHORT).show()
            presenter.getAllTodo()

        }
    }

    override fun onEmptyTodo(state: Boolean) {

        requireActivity().runOnUiThread {
            binding.run {
                tvEmpty.visibility = if (state) View.VISIBLE else View.GONE
                rvList.visibility = if (state) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllTodo()
    }

    override fun onClick(todo: TodoModel) {
        requireActivity().runOnUiThread {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment2(todo)
            findNavController().navigate(action)
        }
    }

    override fun onDone(todo: TodoModel) {
        presenter.getEditTodo(todo)
        cancelAlarmReminder(requireContext(),todo.id.toInt())
    }

    override fun onLongPress(todo: TodoModel, position: Int) {
        val dialog = BottomSheetDialog(requireContext())
        val x = TodoMenuDialogBinding.inflate(LayoutInflater.from(context), null, false)
        x.run {
            tvName.text = todo.title
            tvDone.text = if (todo.isDone) "set as undone" else "set as done"
            tvView.setOnClickListener {
                dialog.hide()
                onClick(todo)
            }
      
            tvDone.setOnClickListener {
                dialog.hide()
                todo.isDone = !todo.isDone
                onDone(todo)
            }
            tvDelete.setOnClickListener {
                dialog.hide()
                presenterDel.getDeleteTodo(todo)
            }

        }
        dialog.setContentView(x.root)
        dialog.show()
        
        
    }
}