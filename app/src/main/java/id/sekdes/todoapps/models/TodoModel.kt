package id.sekdes.todoapps.models

import android.os.Parcelable
import id.sekdes.todoapps.repository.locale.entities.TodoEntity
import id.sekdes.todoapps.views.util.ArrayListConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoModel(
    val id: Long = 0,
    val title: String,
    val images: ArrayList<String?>? = null,
    val voiceNote: String = "",
    val isDone: Boolean = false,
    val dueTime: String = "",
    val reminder: Boolean = false,
    val reminderTime: Double = 0.0
) : Parcelable

fun TodoModel.toEntity() = TodoEntity(id, title, ArrayListConverter.fromArrayList(images), voiceNote, isDone, dueTime, reminder, reminderTime)
