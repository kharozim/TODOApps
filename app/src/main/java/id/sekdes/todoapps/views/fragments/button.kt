//package id.sekdes.todoapps.views.fragments
//
//import android.app.Activity
//import android.media.MediaRecorder
//import android.os.Bundle
//import android.os.Environment
//import android.view.View
//import android.view.View.OnTouchListener
//import android.widget.Button
//import id.sekdes.todoapps.R
//
//class button : Activity() {
//    var b1: Button? = null
//    private var recorder: MediaRecorder? = null
//    private val currentFormat = 0
//    private val output_formats =
//        intArrayOf(MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP)
//    private val file_exts = arrayOf(AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP)
//
//    /** Called when the activity is first created.  */
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.main)
//        b1 = findViewById<View>(R.id.button1) as Button
//        b1!!.setOnTouchListener(OnTouchListener { v, event -> // TODO Auto-generated method stub
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    AppLog.logString("Start Recording")
//                    startRecording()
//                    return@OnTouchListener true
//                }
//                MotionEvent.ACTION_UP -> {
//                    AppLog.logString("stop Recording")
//                    stopRecording()
//                }
//            }
//            false
//        })
//    }
//
//    private val filename: String
//        private get() {
//            val filepath = Environment.getExternalStorageDirectory().path
//            val file = File(filepath, AUDIO_RECORDER_FOLDER)
//            if (!file.exists()) {
//                file.mkdirs()
//            }
//            return file.getAbsolutePath()
//                .toString() + "/" + System.currentTimeMillis() + file_exts[currentFormat]
//        }
//
//    private fun startRecording() {
//        recorder = MediaRecorder()
//        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//        recorder!!.setOutputFormat(output_formats[currentFormat])
//        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//        recorder!!.setOutputFile(filename)
//        recorder!!.setOnErrorListener(errorListener)
//        recorder!!.setOnInfoListener(infoListener)
//        try {
//            recorder!!.prepare()
//            recorder!!.start()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private val errorListener =
//        MediaRecorder.OnErrorListener { mr, what, extra -> AppLog.logString("Error: $what, $extra") }
//    private val infoListener =
//        MediaRecorder.OnInfoListener { mr, what, extra -> AppLog.logString("Warning: $what, $extra") }
//
//    private fun stopRecording() {
//        if (null != recorder) {
//            recorder!!.stop()
//            recorder!!.reset()
//            recorder!!.release()
//            recorder = null
//        }
//    }
//
//    companion object {
//        private const val AUDIO_RECORDER_FILE_EXT_3GP = ".3gp"
//        private const val AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4"
//        private const val AUDIO_RECORDER_FOLDER = "AudioRecorder"
//    }
//}