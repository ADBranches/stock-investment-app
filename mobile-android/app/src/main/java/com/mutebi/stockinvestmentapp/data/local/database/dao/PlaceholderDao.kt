package com.mutebi.stockinvestmentapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mutebi.stockinvestmentapp.data.local.database.entities.PlaceholderEntity

@Dao
interface PlaceholderDao {
    @Query("SELECT * FROM placeholder LIMIT 1")
    suspend fun getPlaceholder(): PlaceholderEntity?
}
