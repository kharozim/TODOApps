package id.sekdes.todoapps.views.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
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
import id.sekdes.todoapps.views.ImagePickerActivity
import id.sekdes.todoapps.views.contracts.TodoAddContract
import id.sekdes.todoapps.views.util.Constant
import id.sekdes.todoapps.views.util.DateUtil
import java.io.File
import java.io.IOException
import java.time.LocalTime
import java.util.*


class AddFragment : Fragment(), TodoAddContract.View {

    private val pickImage = 100
    val REQUEST_IMAGE = 100
    private var imageUri: Uri? = null
    private var fileName = ""
    private lateinit var pickedDateTime: LocalTime

    private lateinit var binding: FragmentAddBinding
    private val dao: TodoDao by lazy { LocaleDatabase.getDatabase(requireContext()).dao() }
    private val repository: TodoLocalRepository by lazy { TodoLocalRepositoryImpl(dao) }
    private val presenter: TodoAddContract.Presenter by lazy {
        TodoAddPresenter(
            this,
            repository
        )
    }


    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    val REQUEST_IMAGE_CAPTURE = 0
    val REQUEST_GALLERY_IMAGE = 1


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setView()
//        setRecorder()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setView() {
        binding.apply {

            btRecord.setOnClickListener {
//                if (ContextCompat.checkSelfPermission(
//                        requireContext(),
//                        Manifest.permission.RECORD_AUDIO
//                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//                        requireContext(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) != PackageManager.PERMISSION_GRANTED) {
//                    val permissions = arrayOf(
//                        android.Manifest.permission.RECORD_AUDIO,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE
//                    )
//                    ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
//                }else{
//                    startRecording()
//                }
            }

            btClose.setOnClickListener {

            }
            btReminder.setOnClickListener {

            }

            btTime.setOnClickListener {
                openDateTimePicker()

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
//                presenter.insertTodo(
//                    TodoModel(
//                        title = etTitle.text.toString(),
//                        description = etTitle.text.toString(),
//                        isDone = cbIsDone.isChecked,
//                        reminder = cbIsReminder.isChecked
//                    )
//                )
            }

        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDateTimePicker() {
        binding.run {
            val instance = Calendar.getInstance()
            val startHour = instance.get(Calendar.HOUR_OF_DAY)
            val startMinute = instance.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(), { _, hour, minute ->
                pickedDateTime =
                    LocalTime.of(
                        hour,
                        minute
                    )

                if (
                    pickedDateTime.hour < startHour) {
                    Toast.makeText(
                        requireContext(),
                        "You are not allowed to specify an earlier time!",
                        Toast.LENGTH_SHORT
                    ).show()
                    btTime.text = Constant.SELECT_DATE
                } else {
                    btTime.text = pickedDateTime.format(DateUtil.timeFormat)
                }
            }, startHour, startMinute, true).show()

        }
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
                try {
                    // You can update this bitmap to your server
//                    val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri)
//                    bytesCover = DbBitmapUtility.getBytes(bitmap)
//
//                    // loading profile image from local cache
//                    loadProfile(uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getCacheImagePath(fileName: String): Uri? {
        val path: File = File(activity?.externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return FileProvider.getUriForFile(
            requireContext(),
            activity?.packageName + ".provider",
            image
        )
    }


//    private fun setRecorder(){
//        output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"
//        mediaRecorder = MediaRecorder()
//
//        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//        mediaRecorder?.setOutputFile(output)
//    }

    override fun onSuccessInsertTodo(todoModel: TodoModel) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, "New Task is assigned", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        }
    }

//    private fun startRecording() {
//        try {
//            mediaRecorder?.prepare()
//            mediaRecorder?.start()
//            state = true
//            Toast.makeText(requireContext(), "Recording started!", Toast.LENGTH_SHORT).show()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun stopRecording(){
//        if(state){
//            mediaRecorder?.stop()
//            mediaRecorder?.release()
//            state = false
//        }else{
//            Toast.makeText(requireContext(), "You are not recording right now!", Toast.LENGTH_SHORT).show()
//        }
//    }
}