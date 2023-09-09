package com.example.todoenhancedui.data.typeconverters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeConverter {

    @TypeConverter
    fun fromTime(time: LocalTime): String {
        var output = ""
        time.toString().forEach {
            if (it.toString()=="."){
                return output
            }
            output += it
        }
        return output
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toTime(time: String): LocalTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.parse(time, formatter)
    }
}