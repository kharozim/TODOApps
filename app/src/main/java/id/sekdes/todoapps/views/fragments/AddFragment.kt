package id.sekdes.todoapps.views.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import id.sekdes.todoapps.databinding.FragmentAddBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoAddPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.views.contracts.TodoAddContract
import id.sekdes.todoapps.views.utils.Constant
import id.sekdes.todoapps.views.utils.DateUtil
import java.time.LocalDateTime
import java.util.*


class AddFragment : Fragment(), TodoAddContract.View {

    private lateinit var binding: FragmentAddBinding
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoAddContract.Presenter by lazy { TodoAddPresenter(this, repository) }

    private lateinit var pickedDateTime: LocalDateTime


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setView()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setView() {
        binding.apply {
            btTime.setOnClickListener { openDateTimePicker() }
//            btTime.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//                    btTime.error = null
//                }
//
//            })

            btSave.setOnClickListener {
                presenter.insertTodo(
                    TodoModel(
                        title = tieName.text.toString(),
                        description = tieDescription.text.toString(),
                        isDone = cbIsDone.isChecked,
                        reminder = cbIsReminder.isChecked
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDateTimePicker() {
        binding.run {
            val instance = Calendar.getInstance()
            val startYear = instance.get(Calendar.YEAR)
            val startMonth = instance.get(Calendar.MONTH)
            val startDay = instance.get(Calendar.DAY_OF_MONTH)
            val startHour = instance.get(Calendar.HOUR_OF_DAY)
            val startMinute = instance.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                TimePickerDialog(requireContext(), { _, hour, minute ->
                pickedDateTime =
                    LocalDateTime.of(
                        year,
                        month + 1,
                        day,
                        hour,
                        minute
                    )

                if (pickedDateTime.dayOfMonth == startDay && pickedDateTime.hour < startHour) {
                    showMessage("You are not allowed to specify an earlier time!")
                    btTime.error = "Select valid time"
                    btTime.text = Constant.SELECT_DATE
                } else {
                    btTime.text = pickedDateTime.format(DateUtil.dateTimeFormat)
                }
            }, startHour, startMinute, true).show()
            }, startYear, startMonth, startDay)

            datePickerDialog.datePicker
                .minDate = System.currentTimeMillis()

            datePickerDialog.show()
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessInsertTodo(todoModel: TodoModel) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, "New Task is assigned", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        }
    }
}

