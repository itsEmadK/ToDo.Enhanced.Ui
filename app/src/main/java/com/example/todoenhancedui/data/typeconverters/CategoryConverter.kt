package com.example.todoenhancedui.data.typeconverters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.todoenhancedui.data.models.Category
class CategoryConverter() {

    @TypeConverter
    fun fromCategory(category: Category):String{
        return category.name
    }

    @TypeConverter
    fun toCategory(category: String):Category{
        return Category.valueOf(category)
    }
}