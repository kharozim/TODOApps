package id.sekdes.todoapps.repository.locale.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.sekdes.todoapps.models.TodoModel

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "images") val images: String,
    @ColumnInfo(name = "voiceNote") val voiceNote: String,
    @ColumnInfo(name = "isDone") val isDone: Boolean = false,
    @ColumnInfo(name = "reminder") val reminder: Boolean = false,
    @ColumnInfo(name = "reminderTime") val reminderTime: Float
)

fun TodoEntity.toModel() = TodoModel(id, title, description, images, voiceNote, isDone, reminder, reminderTime)