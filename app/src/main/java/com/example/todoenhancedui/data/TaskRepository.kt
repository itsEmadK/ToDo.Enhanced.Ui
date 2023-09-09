package com.example.todoenhancedui.data

import androidx.lifecycle.LiveData
import com.example.todoenhancedui.data.models.Task
import java.time.LocalDate

class TaskRepository(private val dao: TaskDao) {

    val allTasks: LiveData<List<Task>> = dao.getAllData()
    suspend fun insertTask(task: Task) {
        dao.insert(task)
    }

    suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }

    suspend fun updateTask(task: Task) {
        dao.update(task)
    }
    fun getTasksForDateOrderByTime(date:LocalDate?):LiveData<List<Task>>{
        return dao.getDataForDateOrderByTime(date)
    }
}