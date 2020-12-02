package id.sekdes.todoapps.models

import android.os.Parcelable
import id.sekdes.todoapps.repository.locale.entities.TodoEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoModel(
    val id: Long?,
    val title: String,
    val description: String,
    val images: String,
    val voiceNote: String,
    val isDone: Boolean = false,
    val reminder: Boolean = false,
    val reminderTime: Double = 0.0
) : Parcelable

fun TodoModel.toEntity() =
    TodoEntity(title, description, images, voiceNote, isDone, reminder, reminderTime)

/*
fun TodoModel.toEntity() =
    TodoListPastEntity(id, title, description, images, voiceNote, isDone, reminder, reminderTime)*/
