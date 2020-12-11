package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sekdes.todoapps.databinding.FragmentPastBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoPastPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.views.adapters.TodoPastAdapter
import id.sekdes.todoapps.views.contracts.TodoPastContract

class PastFragment : Fragment(), TodoPastContract.View , TodoPastAdapter.TodoListener{

    private lateinit var binding: FragmentPastBinding
    private val adapter by lazy { TodoPastAdapter(requireContext(), this) }
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoPastContract.Presenter by lazy { TodoPastPresenter(this,repository) }

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
    }

    override fun onDelete(todo: TodoModel, position: Int) {
    }

    override fun onFavorite(todo: TodoModel) {
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

    override fun onEmptyTodo(state: Boolean) {
    }

}