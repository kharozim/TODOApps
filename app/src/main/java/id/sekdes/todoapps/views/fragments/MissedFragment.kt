package id.sekdes.todoapps.views.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentMissedBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoListPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.views.adapters.TodoMissedAdapter
import id.sekdes.todoapps.views.adapters.TodoTodayAdapter
import id.sekdes.todoapps.views.contracts.TodoListContract

class MissedFragment : Fragment(), TodoMissedAdapter.TodoListener, TodoListContract.View  {

    private lateinit var binding : FragmentMissedBinding
    private val adapter by lazy { TodoMissedAdapter(requireContext(), this) }
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoListContract.Presenter by lazy { TodoListPresenter(this,repository) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissedBinding.inflate(inflater, container, false)

        setView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllTodo()
    }

    private fun setView(){
        binding.run {
            rvMissedTodo.adapter = adapter
        }
    }

    override fun onClick(todo: TodoModel) {

    }

    override fun onDelete(todo: TodoModel, position: Int) {

    }

    override fun onLongPress(todo: TodoModel, position: Int) {

    }

    override fun loading(state: Boolean) {

    }

    override fun onSuccessGetAllTodo(list: List<TodoModel>) {
        requireActivity().runOnUiThread {
            adapter.generateTodo(list.toMutableList())
        }
    }

    override fun onSuccessUpdateTodo(todoModel: TodoModel) {

    }

    override fun onEmptyTodo(state: Boolean) {
        requireActivity().runOnUiThread {
            binding.run {
                tvEmptyMissed.visibility = if (state) View.VISIBLE else View.GONE
                rvMissedTodo.visibility = if (state) View.GONE else View.VISIBLE
            }
        }
    }

}