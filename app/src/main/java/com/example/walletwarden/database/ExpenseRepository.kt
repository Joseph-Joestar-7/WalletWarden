package com.example.walletwarden.database

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

    fun getMonthName(monthId: Int): Flow<String> {
        return expenseDao.getMonthName(monthId)
    }

    fun getYear(monthId: Int): Flow<Int> {
        return expenseDao.getYear(monthId)
    }

//    suspend fun update(expense: ExpenseEntity) {
//        expenseDao.update(expense)
//    }

    suspend fun delete(expenseId: Int) {
        expenseDao.delete(expenseId)
    }
}