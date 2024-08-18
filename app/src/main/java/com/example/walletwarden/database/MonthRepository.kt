package com.example.walletwarden.database

import kotlinx.coroutines.flow.Flow

class MonthRepository(private val monthDao: MonthDao) {
    val allMonths: Flow<List<MonthEntity>> = monthDao.getAllMonths()

    suspend fun insert(month: MonthEntity) {
        monthDao.insert(month)
    }

    suspend fun update(month: MonthEntity) {
        monthDao.update(month)
    }

    suspend fun delete(month: MonthEntity) {
        monthDao.delete(month)
    }
}