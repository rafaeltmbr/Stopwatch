package com.rafaeltmbr.stopwatch.infra.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rafaeltmbr.stopwatch.infra.data.room.entities.LapEntity

@Dao
interface LapsDao {
    @Query("SELECT * FROM laps")
    suspend fun load(): List<LapEntity>

    @Insert
    suspend fun save(laps: List<LapEntity>)

    @Query("DELETE FROM laps")
    suspend fun clear()
}