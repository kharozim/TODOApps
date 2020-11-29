package id.sekdes.todoapps.models

import android.os.Parcelable
import id.sekdes.todoapps.repository.locale.entities.TodoEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoModel(
    val id: Long,
    val title: String,
    val description: String,
    val images: List<String>,
    val voiceNote: String,
    val isDone: Boolean = false,
    val reminder: Boolean = false,
    val reminderTime: Float
) : Parcelable

fun TodoModel.toEntity(): TodoEntity =
    TodoEntity(id, title, description, images, voiceNote, isDone, reminder, reminderTime)