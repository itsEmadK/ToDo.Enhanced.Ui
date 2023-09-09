package com.example.todoenhancedui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoenhancedui.data.Constants.DATABASE_NAME
import com.example.todoenhancedui.data.models.Task
import com.example.todoenhancedui.data.typeconverters.CategoryConverter
import com.example.todoenhancedui.data.typeconverters.LocalDateConverter
import com.example.todoenhancedui.data.typeconverters.LocalTimeConverter

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(CategoryConverter::class, LocalDateConverter::class, LocalTimeConverter::class)
abstract class TaskDatabase() : RoomDatabase() {

    abstract fun getDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null
        fun getDatabaseInstance(context: Context): TaskDatabase {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            synchronized(this) {
                INSTANCE =
                    Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        DATABASE_NAME
                    ).build()
            }
            return INSTANCE!!
        }
    }
}