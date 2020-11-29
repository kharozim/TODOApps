package id.sekdes.todoapps.repository.locale.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.sekdes.todoapps.models.TodoModel

@Entity(tableName = "todo_list")
data class TodoListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "images") val images: List<String>,
    @ColumnInfo(name = "voiceNote") val voiceNote: String,
    @ColumnInfo(name = "isDone") val isDone: Boolean = false,
    @ColumnInfo(name = "reminder") val reminder: Boolean = false,
    @ColumnInfo(name = "reminderTime") val reminderTime: Float
)

fun TodoListEntity.toModel(): TodoModel =
    TodoModel(id, title, description, images, voiceNote, isDone, reminder, reminderTime)