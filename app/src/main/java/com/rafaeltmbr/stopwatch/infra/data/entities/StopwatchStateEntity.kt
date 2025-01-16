package com.rafaeltmbr.stopwatch.infra.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopwatch_state")
data class StopwatchStateEntity(
    @PrimaryKey val id: Int,
    val milliseconds: Long,
)
