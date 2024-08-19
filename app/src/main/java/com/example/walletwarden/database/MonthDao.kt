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

    @Query("UPDATE MonthEntity SET month = :monthName, year = :year WHERE id = :id")
    suspend fun update(id:Int,monthName: String,year:Int)

    @Delete
    suspend fun delete(month: MonthEntity)

    @Query("SELECT * FROM MonthEntity ORDER BY year ASC, monthNo ASC")
    fun getAllMonths(): Flow<List<MonthEntity>>
}