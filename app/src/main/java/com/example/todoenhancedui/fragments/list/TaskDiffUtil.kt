package com.example.todoenhancedui.fragments.list

import androidx.recyclerview.widget.DiffUtil
import com.example.todoenhancedui.data.models.Task

class TaskDiffUtil(private val oldList: List<Task>, private val newList: List<Task>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}