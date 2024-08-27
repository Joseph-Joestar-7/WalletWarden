package com.example.walletwarden.database

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getExpensesForMonth(monthId: Int): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesForMonth(monthId)
    }

    fun getTotalExpenseForMonth(monthId: Int): Flow<Int> {
        return expenseDao.getTotalExpenseForMonth(monthId)
    }

    suspend fun insert(expense: ExpenseEntity) {
        expenseDao.insert(expense)
    }

    suspend fun getMonthName(monthId: Int): String {
        val monthName = expenseDao.getMonthName(monthId)
        return monthName
    }

    suspend fun getYear(monthId: Int): Int {
        val year = expenseDao.getYear(monthId)
        Log.d("Repository", "Year name fetched: $year")
        return year
    }

//    suspend fun update(expense: ExpenseEntity) {
//        expenseDao.update(expense)
//    }

    suspend fun delete(expenseId: Int) {
        expenseDao.delete(expenseId)
    }
}