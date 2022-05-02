package com.redcatgames.movies.data.source.local.converter

import androidx.room.TypeConverter

object ListConverter {

    private const val SEPARATOR = ","

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(SEPARATOR)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(
            separator = SEPARATOR
        )
    }
}