package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.sekdes.todoapps.contract.TodoAddContract
import id.sekdes.todoapps.databinding.FragmentAddBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoAddPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase


class AddFragment : Fragment(), TodoAddContract.View {

    private lateinit var binding : FragmentAddBinding
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoAddContract.Presenter by lazy { TodoAddPresenter(
        this,
        repository
    ) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setView()

        return binding.root
    }

    private fun setView(){
        binding.apply {
            btnAdd.setOnClickListener {
                presenter.insertTodo(
                    TodoModel(
                        null,
                        tieTitle.text.toString(),
                        tieDescription.text.toString(),
                        tieImage.text.toString(),
                        tieRecord.text.toString()
                    )
                )
            }
        }
    }

    override fun onSuccessInsertTodo(todoModel: TodoModel) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, "New Task is assigned", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        }
    }
}