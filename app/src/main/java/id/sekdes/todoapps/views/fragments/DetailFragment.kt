package id.sekdes.todoapps.views.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import id.sekdes.todoapps.views.contracts.TodoEditContract
import id.sekdes.todoapps.databinding.FragmentDetailBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoEditPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.views.adapters.ImageAdapter
import id.sekdes.todoapps.views.adapters.ImageDetailAdapter


class DetailFragment : Fragment(), TodoEditContract.View, ImageAdapter.ImageListener {

    private val args by navArgs<DetailFragmentArgs>()
    private val imageAdapter by lazy { ImageDetailAdapter(requireContext(), this) }

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

            rvImage.adapter = imageAdapter

            etTitle.setText(args.todo.title)
            tvTime.text = args.todo.dueTime
            args.todo.images?.asSequence()?.map { Uri.parse(it) }
                ?.toMutableList()?.let { imageAdapter.setData(it) }

            btUpdate.setOnClickListener {
                presenter.getEditTodo(
                    TodoModel(
                        args.todo.id,
                        title = etTitle.text.toString(),
                        images = args.todo.images,
                        dueDate = args.todo.dueDate,
                        dueTime = args.todo.dueTime,
                        reminder = args.todo.reminder,
                        isDone = args.todo.isDone,
                        reminderTime = args.todo.reminderTime,
                        voiceNote = args.todo.voiceNote
                    )
                )
            }

            btClose.setOnClickListener {
                activity?.onBackPressed()
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

    override fun onClick(uri: Uri) {

        val action = DetailFragmentDirections.actionDetailFragmentToImageFragment(uri.toString())
        findNavController().navigate(action)
    }

    override fun onDelete(uri: Uri) {
        TODO("Not yet implemented")
    }



}