package com.example.todoenhancedui.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoenhancedui.data.models.Task
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM  task_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE (date LIKE :dateArg OR date IS NULL) AND(completedDate LIKE :dateArg OR completedDate IS NULL) ORDER BY time ASC")
    fun getDataForDateOrderByTime(dateArg:LocalDate?):LiveData<List<Task>>

    @Insert(entity = Task::class, OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Delete(entity = Task::class)
    suspend fun delete(task: Task)

    @Update(entity = Task::class)
    suspend fun update(task: Task)

}