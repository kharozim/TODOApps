package id.sekdes.todoapps.repository.locale.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.sekdes.todoapps.repository.locale.daos.TodoDao
import id.sekdes.todoapps.repository.locale.entities.TodoEntity

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class LocaleDatabase : RoomDatabase() {
    abstract fun dao(): TodoDao

    companion object {
        @Volatile
        private lateinit var localeDatabase: LocaleDatabase

        private const val DATABASE_NAME = "local_todo.db"

        fun getDatabase(context: Context): LocaleDatabase {
            if (!this::localeDatabase.isInitialized) {
                localeDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    LocaleDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return localeDatabase
        }
    }


}