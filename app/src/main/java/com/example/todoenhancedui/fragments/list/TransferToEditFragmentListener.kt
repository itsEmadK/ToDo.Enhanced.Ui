package com.example.todoenhancedui.fragments.list

import com.example.todoenhancedui.data.models.Task

interface TransferToEditFragmentListener {
    fun onTransfer(task:Task)
}