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

    @Query("UPDATE months SET month = :monthName, year = :year,monthNo= :moNo WHERE id = :id")
    suspend fun update(id:Int,monthName: String,year:Int,moNo:Int)

    @Delete
    suspend fun delete(month: MonthEntity)

    @Query("SELECT * FROM months ORDER BY year ASC, monthNo ASC")
    fun getAllMonths(): Flow<List<MonthEntity>>

    @Query("SELECT month FROM months WHERE id = :id")
    suspend fun getMonthNameById(id: Int): String

    @Query("SELECT year FROM months WHERE id = :id")
    suspend fun getYearById(id: Int): Int
}