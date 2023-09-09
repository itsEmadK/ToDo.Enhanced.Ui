package com.example.todoenhancedui.data.viewmodels

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime

class SharedViewModel : ViewModel() {

    @SuppressLint("SetTextI18n")
    fun showDatePickerDialog(
        dateEditText: EditText?,
        defYear: Int,
        defMonth: Int,
        defDay: Int,
        context: Context
    ) {
        val datePicker = DatePickerDialog(context)
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            dateEditText?.setText("$year/${month+1}/$dayOfMonth")
        }
        datePicker.updateDate(defYear, defMonth-1, defDay)
        datePicker.show()
    }

    @SuppressLint("SetTextI18n")
    fun showTimePickerDialog(
        timeEditText: EditText?,
        defHour: Int,
        defMin: Int,
        context: Context
    ) {

        val timePicker = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                timeEditText?.setText("$hourOfDay:$minute")
            }, defHour, defMin, true
        )
        timePicker.show()
    }

    fun parseToLocalDate(date: String): LocalDate? {
        return if (date.isNotEmpty()) {
            val year = date.split("/")[0].toInt()
            val month = date.split("/")[1].toInt()
            val day = date.split("/")[2].toInt()
            LocalDate.of(year, month, day)
        } else null
    }

    fun parseToLocalTime(time: String): LocalTime? {
        return if (time.isNotEmpty()) {
            val hour = time.split(":")[0].toInt()
            val minute = time.split(":")[1].toInt()
            LocalTime.of(hour, minute)
        } else null
    }

    fun verifyUserInputs(title: String): Boolean = title.isNotEmpty()
     fun dateToString(date: LocalDate): String =
        "${date.year}/${date.monthValue}/${date.dayOfMonth}"

}