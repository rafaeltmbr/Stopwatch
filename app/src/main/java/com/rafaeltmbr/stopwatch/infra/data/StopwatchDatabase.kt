package com.rafaeltmbr.stopwatch.infra.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafaeltmbr.stopwatch.infra.data.dao.LapsDaoRoom
import com.rafaeltmbr.stopwatch.infra.data.dao.StopwatchStateDaoRoom
import com.rafaeltmbr.stopwatch.infra.data.database_entities.LapEntity
import com.rafaeltmbr.stopwatch.infra.data.database_entities.StopwatchStateEntity

@Database(entities = [StopwatchStateEntity::class, LapEntity::class], version = 1)
abstract class StopwatchDatabase : RoomDatabase() {
    abstract fun stopwatchStateDao(): StopwatchStateDaoRoom
    abstract fun lapsDao(): LapsDaoRoom

    companion object {
        private var instance: StopwatchDatabase? = null

        fun getInstance(context: Context): StopwatchDatabase = synchronized(this) {
            if (instance != null) return instance!!

            instance = Room
                .databaseBuilder(context, StopwatchDatabase::class.java, "stopwatch")
                .build()

            return instance!!
        }
    }
}