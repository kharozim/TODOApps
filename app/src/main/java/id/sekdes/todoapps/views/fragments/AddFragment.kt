package id.sekdes.todoapps.views.fragments


import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentAddBinding
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.presenter.TodoAddPresenter
import id.sekdes.todoapps.repository.TodoLocalRepository
import id.sekdes.todoapps.repository.locale.TodoLocalRepositoryImpl
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.databases.LocaleDatabase
import id.sekdes.todoapps.services.AlarmRemainderService.Companion.TIME_FORMAT
import id.sekdes.todoapps.services.AlarmRemainderService.Companion.setAlarmReminder
import id.sekdes.todoapps.views.ImagePickerActivity
import id.sekdes.todoapps.views.adapters.ImageAdapter
import id.sekdes.todoapps.views.contracts.TodoAddContract
import id.sekdes.todoapps.views.util.Constant
import id.sekdes.todoapps.views.util.DateUtil
import id.sekdes.todoapps.views.util.ReminderTime
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddFragment : Fragment(), TodoAddContract.View, ImageAdapter.ImageListener {

    private val REQUEST_IMAGE = 100
    private val pickerTimeAlt: Calendar = Calendar.getInstance()
    private val imageAdapter by lazy { ImageAdapter(requireContext(), this) }
    private lateinit var binding: FragmentAddBinding
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoAddContract.Presenter by lazy { TodoAddPresenter(this, repository) }
    private val imageList = mutableListOf<Uri>()
    private var reminderSet = ReminderTime.BEFORE15

    private var isReminderActive = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setView()

        return binding.root
    }

    private fun setView() {
        binding.apply {

            rvImage.adapter = imageAdapter


            btRecord.setOnClickListener {

            }

            btClose.setOnClickListener {
                activity?.onBackPressed()
            }

            btTime.setOnClickListener {
                openTimePicker()

            }

            mCheckBoxReminder.setOnClickListener {
                isReminderActive = mCheckBoxReminder.isChecked

                spinner.isEnabled = isReminderActive
            }

            ibAddImage.setOnClickListener {

                Dexter.withActivity(activity)
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions()
                            }
                            if (report.isAnyPermissionPermanentlyDenied) {
                                showSettingsDialog()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()

            }


            btSave.setOnClickListener {
                if (etTitle.text.isNullOrEmpty()) {
                    showMessage("Title tidak boleh kosong")
                } else {
                    val todo = TodoModel(
                        title = etTitle.text.toString(),
                        dueDate = getCurrentDate(),
                        dueTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(
                            pickerTimeAlt.time
                        ),
                        images = ArrayList(imageList.asSequence().map { it.toString() }.toList()),
                        reminder = isReminderActive,
                        reminderTime = reminderSet.time


                    )
                    presenter.insertTodo(todo)

                }
            }

        }


        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.reminder_list, android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        var initAdapter = true
        binding.run {
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    reminderSet = when (position) {
                        0 -> ReminderTime.BEFORE15
                        1 -> ReminderTime.BEFORE30
                        2 -> ReminderTime.BEFORE45
                        else -> ReminderTime.BEFORE15
                    }

                    if (!isCanSetTime(
                            pickerTimeAlt.get(Calendar.HOUR_OF_DAY),
                            pickerTimeAlt.get(Calendar.MINUTE)
                        ) && !initAdapter
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Tidak bisa mengatur reminder",
                            Toast.LENGTH_SHORT
                        ).show()
                        btTime.text = Constant.SELECT_DATE
                    } else
                        btTime.text = SimpleDateFormat(
                            TIME_FORMAT,
                            Locale.getDefault()
                        ).format(pickerTimeAlt.time)
                    initAdapter = false
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun getCurrentDate(): String {
        val date = Calendar.getInstance()
        return SimpleDateFormat(DateUtil.dateFormat, Locale.getDefault()).format(date.time)
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            requireContext(),
            object : ImagePickerActivity.PickerOPtionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }

    private fun openTimePicker() {
        binding.run {
            val instance = Calendar.getInstance()
            val startHour = instance.get(Calendar.HOUR_OF_DAY)
            val startMinute = instance.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, hour, minute ->

                pickerTimeAlt.set(Calendar.HOUR_OF_DAY, hour)
                pickerTimeAlt.set(Calendar.MINUTE, minute)


                if (!isCanSetTime(hour, minute)) {
                    Toast.makeText(
                        requireContext(),
                        "Jam Sudah Terlewat",
                        Toast.LENGTH_SHORT
                    ).show()
                    btTime.text = Constant.SELECT_DATE
                } else {
                    btTime.text = SimpleDateFormat(
                        TIME_FORMAT,
                        Locale.getDefault()
                    ).format(pickerTimeAlt.time)
                }
            }, startHour, startMinute, true).show()

        }
    }

    private fun isCanSetTime(hour: Int, minute: Int): Boolean {
        val instance = Calendar.getInstance()
        val startHour = instance.get(Calendar.HOUR_OF_DAY)
        val startMinute = instance.get(Calendar.MINUTE)

        var isSetTime = false


        if (isReminderActive){
            var minuteReminder = minute.minus(reminderSet.time)
            var hourReminder = hour
            if (minuteReminder<0){
                hourReminder = hour-1
                minuteReminder = 60.plus(minuteReminder)
            }


            if (hourReminder == startHour)
                isSetTime = minuteReminder > startMinute


            if (hourReminder > startHour){
                isSetTime = true
            }
        }else{
            isSetTime = !isReminderActive

        }

        binding.btSave.isEnabled = isSetTime

        return isSetTime
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


    private fun launchCameraIntent() {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 3)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 100)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 150)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 3)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 100)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 150)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                imageList.add(uri!!)
                try {
                    imageAdapter.setData(imageList)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


//    private fun getCacheImagePath(fileName: String): Uri? {
//        val path = File(activity?.externalCacheDir, "camera")
//        if (!path.exists()) path.mkdirs()
//        val image = File(path, fileName)
//        return FileProvider.getUriForFile(
//            requireContext(),
//            activity?.packageName + ".provider",
//            image
//        )
//    }


    override fun onSuccessInsertTodo(todoModel: TodoModel) {

        requireActivity().runOnUiThread {
            setAlarmReminder(requireContext(), todoModel)
            Toast.makeText(context, "New Task is assigned: ${todoModel.title}", Toast.LENGTH_LONG)
                .show()
            requireActivity().onBackPressed()
        }
    }

    override fun onClick(uri: Uri) {
        val action = AddFragmentDirections.actionAddFragmentToImageFragment(uri.toString())
        findNavController().navigate(action)
    }

    override fun onDelete(uri: Uri) {
        imageAdapter.deleteData(uri)
    }


}

