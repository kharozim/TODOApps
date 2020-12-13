package id.sekdes.todoapps.repository.locale.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.sekdes.todoapps.models.TodoModel
<<<<<<< HEAD
import java.time.LocalTime
=======
import id.sekdes.todoapps.views.util.ArrayListConverter
>>>>>>> d23ac48086afbb7d34cda6d7f452f6d5de87262c

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "images") val images: String? = null,
    @ColumnInfo(name = "voiceNote") val voiceNote: String = "",
    @ColumnInfo(name = "isDone") val isDone: Boolean = false,
<<<<<<< HEAD
    @ColumnInfo(name = "dueTime")  val dueTime: LocalTime,
=======
    @ColumnInfo(name = "dueDate") val dueDate: String = "",
    @ColumnInfo(name = "dueTime")  val dueTime: String = "",
>>>>>>> d23ac48086afbb7d34cda6d7f452f6d5de87262c
    @ColumnInfo(name = "reminder") val reminder: Boolean = false,
    @ColumnInfo(name = "reminderTime") val reminderTime: Int = 0

){
    companion object
}

fun TodoEntity.toModel() = TodoModel(id, title, ArrayListConverter.fromString(images), voiceNote, isDone, dueDate, dueTime, reminder, reminderTime)