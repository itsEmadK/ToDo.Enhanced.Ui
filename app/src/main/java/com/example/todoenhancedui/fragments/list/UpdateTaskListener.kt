package com.example.todoenhancedui.fragments.list

import com.example.todoenhancedui.data.models.Task

interface UpdateTaskListener {
    fun onUpdate(task: Task)
}