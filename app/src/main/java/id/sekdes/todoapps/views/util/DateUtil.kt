package id.sekdes.todoapps.views.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
object DateUtil {
    val dateFormat_simple = DateTimeFormatter.ofPattern("d.LLL")
    val dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM-yyyy HH:mm")
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val dateTimeFormat_simple = DateTimeFormatter.ofPattern("d.LLL HH:mm")
}