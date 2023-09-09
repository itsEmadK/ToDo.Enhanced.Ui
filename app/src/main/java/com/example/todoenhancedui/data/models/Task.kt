package com.example.todoenhancedui.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoenhancedui.data.Constants
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
@Entity(Constants.TASK_TABLE_NAME)
data class Task(
    var title: String,
    var category: Category,
    var date: LocalDate?,
    var time: LocalTime?,
    var isCompleted: Boolean,
    var notes:String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
):Parcelable
