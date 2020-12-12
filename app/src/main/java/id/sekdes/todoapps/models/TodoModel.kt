package id.sekdes.todoapps.models

import android.os.Parcelable
import id.sekdes.todoapps.repository.locale.entities.TodoEntity
import kotlinx.android.parcel.Parcelize
import java.time.LocalTime

@Parcelize
data class TodoModel(
    val id: Long = 0,
    val title: String,
    val images: String ="",
    val voiceNote: String = "",
    val isDone: Boolean = false,
    val dueTime: LocalTime,
    val reminder: Boolean = false,
    val reminderTime: Double = 0.0
) : Parcelable

fun TodoModel.toEntity() = TodoEntity(id, title, images, voiceNote, isDone, dueTime, reminder, reminderTime)
