package com.rafaeltmbr.stopwatch.infra.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafaeltmbr.stopwatch.infra.data.room.dao.LapsDao
import com.rafaeltmbr.stopwatch.infra.data.room.dao.StopwatchStateDao
import com.rafaeltmbr.stopwatch.infra.data.room.entities.LapEntity
import com.rafaeltmbr.stopwatch.infra.data.room.entities.StopwatchStateEntity

@Database(entities = [StopwatchStateEntity::class, LapEntity::class], version = 1)
abstract class StopwatchDatabase : RoomDatabase() {
    abstract fun stopwatchStateDao(): StopwatchStateDao
    abstract fun lapsDao(): LapsDao

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