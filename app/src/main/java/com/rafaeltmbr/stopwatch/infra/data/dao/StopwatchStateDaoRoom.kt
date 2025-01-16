package com.rafaeltmbr.stopwatch.infra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rafaeltmbr.stopwatch.infra.data.database_entities.StopwatchStateEntity

@Dao
interface StopwatchStateDaoRoom {
    @Query("SELECT * FROM stopwatch_state LIMIT 1")
    suspend fun load(): StopwatchStateEntity?

    @Insert
    suspend fun save(state: StopwatchStateEntity)

    @Query("DELETE FROM stopwatch_state")
    suspend fun clear()
}