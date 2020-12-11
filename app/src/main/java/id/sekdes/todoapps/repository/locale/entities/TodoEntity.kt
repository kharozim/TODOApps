package id.sekdes.todoapps.repository.locale.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.sekdes.todoapps.models.TodoModel
import id.sekdes.todoapps.views.util.ArrayListConverter

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "images") val images: String? = null,
    @ColumnInfo(name = "voiceNote") val voiceNote: String = "",
    @ColumnInfo(name = "isDone") val isDone: Boolean = false,
    @ColumnInfo(name = "dueDate") val dueDate: String = "",
    @ColumnInfo(name = "dueTime")  val dueTime: String = "",
    @ColumnInfo(name = "reminder") val reminder: Boolean = false,
    @ColumnInfo(name = "reminderTime") val reminderTime: Double = 0.0

){
    companion object
}

fun TodoEntity.toModel() = TodoModel(id, title, ArrayListConverter.fromString(images), voiceNote, isDone, dueDate, dueTime, reminder, reminderTime)