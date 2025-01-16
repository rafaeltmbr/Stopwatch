package com.rafaeltmbr.stopwatch.infra.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laps")
data class LapEntity(
    @PrimaryKey val index: Int,
    val milliseconds: Long,
)
