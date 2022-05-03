package com.redcatgames.movies.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey
    val iso : String,
    val englishName : String,
    val nativeName : String,
    val created: Date
)
