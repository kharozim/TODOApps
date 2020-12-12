package id.sekdes.todoapps.views.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.sekdes.todoapps.databinding.FragmentPastBinding
import id.sekdes.todoapps.databinding.TodoMenuDialogBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoDeletePresenter
import id.sekdes.todoapps.presenter.TodoPastPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.views.adapters.TodoPastAdapter
import id.sekdes.todoapps.views.contracts.TodoDeleteContract
import id.sekdes.todoapps.views.contracts.TodoPastContract

class PastFragment :
    Fragment(),
    TodoPastContract.View ,
    TodoDeleteContract.View,
    TodoPastAdapter.TodoListener{

    private lateinit var binding: FragmentPastBinding
    private val adapter by lazy { TodoPastAdapter(requireContext(), this) }
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoPastContract.Presenter by lazy { TodoPastPresenter(this,repository) }
    private val presenterDel : TodoDeleteContract.Presenter by lazy { TodoDeletePresenter(this,repository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPastBinding.inflate(inflater, container, false)

        setView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllTodo()
    }

    private fun setView(){
        binding.run {
            rvPastTodo.adapter = adapter
        }
    }

    override fun onClick(todo: TodoModel) {
        requireActivity().runOnUiThread {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment2(todo)
            findNavController().navigate(action)
        }
    }

    override fun onDelete(todo: TodoModel, position: Int) {
    }

    override fun onDone(todo: TodoModel) {
        presenter.getEditTodo(todo)
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

    override fun loading(state: Boolean) {
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
        }
    }

    override fun onEmptyTodo(state: Boolean) {
    }

    override fun onSuccessUpdateTodo(todoModel: TodoModel) {
        requireActivity().runOnUiThread {
            adapter.updateData(todoModel)
            Toast.makeText(requireContext(), "${todoModel.id} berhasil diubah", Toast.LENGTH_SHORT).show()
            presenter.getAllTodo()

        }
    }

}