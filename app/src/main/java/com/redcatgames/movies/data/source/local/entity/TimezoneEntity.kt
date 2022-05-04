package com.redcatgames.movies.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "timezones")
data class TimezoneEntity(
    @PrimaryKey
    val iso : String,
    val zoneName : String,
    val created: Date
)
