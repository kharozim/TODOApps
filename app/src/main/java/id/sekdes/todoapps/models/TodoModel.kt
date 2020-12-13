package id.sekdes.todoapps.models

import android.os.Parcelable
import id.sekdes.todoapps.repository.locale.entities.TodoEntity
import id.sekdes.todoapps.views.util.ArrayListConverter
import kotlinx.android.parcel.Parcelize
import java.time.LocalTime

@Parcelize
data class TodoModel(
    val id: Long = 0,
    val title: String,
    val images: ArrayList<String?>? = null,
    val voiceNote: String = "",
    var isDone: Boolean = false,
    val dueDate: String = "",
    val dueTime: String = "",
    var reminder: Boolean = true,
    val reminderTime: Int = 0
) : Parcelable

fun TodoModel.toEntity() = TodoEntity(id, title, ArrayListConverter.fromArrayList(images), voiceNote, isDone, dueDate, dueTime, reminder, reminderTime)
