package com.example.todoenhancedui.fragments.list

import com.example.todoenhancedui.data.models.Task

interface TaskCompletedStatusChangedListener {
    fun onTaskCompletedStatusChanged(task: Task)
}