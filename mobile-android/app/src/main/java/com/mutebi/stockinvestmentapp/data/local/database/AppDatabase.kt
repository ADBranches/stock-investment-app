package com.mutebi.stockinvestmentapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mutebi.stockinvestmentapp.data.local.database.dao.PlaceholderDao
import com.mutebi.stockinvestmentapp.data.local.database.entities.PlaceholderEntity

@Database(
    entities = [PlaceholderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeholderDao(): PlaceholderDao
}
