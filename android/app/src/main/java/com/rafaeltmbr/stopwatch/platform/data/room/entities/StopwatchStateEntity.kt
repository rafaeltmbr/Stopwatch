package com.rafaeltmbr.stopwatch.platform.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopwatch_state")
data class StopwatchStateEntity(
    @PrimaryKey val id: Int,
    val status: Int,
    val milliseconds: Long,
)
