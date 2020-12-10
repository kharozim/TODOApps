package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import id.sekdes.todoapps.views.contracts.TodoEditContract
import id.sekdes.todoapps.databinding.FragmentDetailBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoEditPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase

class DetailFragment : Fragment(), TodoEditContract.View {

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var binding: FragmentDetailBinding
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoEditContract.Presenter by lazy {
        TodoEditPresenter(
            this,
            repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {

//            tieName.setText(args.todo.title)
//            tieDescription.setText(args.todo.description)
            etTitle.setText(args.todo.title)
            btTime.text = args.todo.dueTime


            btUpdate.setOnClickListener {
                presenter.getEditTodo(
                    TodoModel(
                        args.todo.id,
                        title = etTitle.text.toString(),
                        dueTime = btTime.text.toString()
                    )
                )
            }
        }
        return binding.root
    }

    override fun loading(state: Boolean) {

    }

    override fun onSuccessEditTodo(todoModel: TodoModel) {

        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(),
                "${todoModel.id} berhasil diupdate",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onEmptyTodo(state: Boolean) {
        TODO("Not yet implemented")
    }
}