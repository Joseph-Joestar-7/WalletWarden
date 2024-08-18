package com.example.walletwarden.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao{
    @Insert
    suspend fun insert(month:MonthEntity)

    @Update
    suspend fun update(month: MonthEntity)

    @Delete
    suspend fun delete(month: MonthEntity)

    @Query("SELECT * FROM MonthEntity ORDER BY id ASC")
    fun getAllMonths(): Flow<List<MonthEntity>>
}