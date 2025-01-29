package com.rafaeltmbr.stopwatch.platform.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laps")
data class LapEntity(
    @PrimaryKey val index: Int,
    val milliseconds: Long,
    val status: Int
)
