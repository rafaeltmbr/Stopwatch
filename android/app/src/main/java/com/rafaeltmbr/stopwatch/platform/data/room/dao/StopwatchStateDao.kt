package com.rafaeltmbr.stopwatch.platform.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rafaeltmbr.stopwatch.platform.data.room.entities.StopwatchStateEntity

@Dao
interface StopwatchStateDao {
    @Query("SELECT * FROM stopwatch_state LIMIT 1")
    suspend fun load(): StopwatchStateEntity?

    @Insert
    suspend fun save(state: StopwatchStateEntity)

    @Query("DELETE FROM stopwatch_state")
    suspend fun clear()
}