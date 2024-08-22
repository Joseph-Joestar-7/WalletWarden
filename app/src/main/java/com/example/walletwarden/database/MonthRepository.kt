package com.example.walletwarden.database

import kotlinx.coroutines.flow.Flow

class MonthRepository(private val monthDao: MonthDao) {
    val allMonths: Flow<List<MonthEntity>> = monthDao.getAllMonths()

    suspend fun insert(month: MonthEntity) {
        monthDao.insert(month)
    }

    suspend fun update(monthName: String,year:Int,moNo:Int,month: MonthEntity) {
        monthDao.update(month.id,monthName,year,moNo)
    }

    suspend fun delete(month: MonthEntity) {
        monthDao.delete(month)
    }

    suspend fun getMonthNameById(id: Int): String {
        return monthDao.getMonthNameById(id)
    }

    suspend fun getYearById(id: Int): Int {
        return monthDao.getYearById(id)
    }
}