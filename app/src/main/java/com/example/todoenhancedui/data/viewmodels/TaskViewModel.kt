package com.example.todoenhancedui.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.todoenhancedui.data.TaskDatabase
import com.example.todoenhancedui.data.TaskRepository
import com.example.todoenhancedui.data.models.Task
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(context: Application) : AndroidViewModel(context) {

    private val database = TaskDatabase.getDatabaseInstance(context)
    private val repository = TaskRepository(database.getDao())

    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())
    var selectedDate: LocalDate
        get() = _selectedDate.value!!
        set(value) {
            _selectedDate.value = value
        }

    val tasksForDate=_selectedDate.switchMap {
        getTasksForDateOrderByTime(it)
    }
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun getCompletedTasks(allTasks: List<Task>): List<Task> {
        return allTasks.filter { it.isCompleted }
    }

    fun getOpenTasks(allTasks: List<Task>): List<Task> {
        return allTasks.filter { !it.isCompleted }
    }

    private fun getTasksForDateOrderByTime(date: LocalDate?): LiveData<List<Task>> {
        return repository.getTasksForDateOrderByTime(date)
    }


}