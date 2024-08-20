package com.example.walletwarden.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun delete(expenseId: Int)

    @Query("SELECT * FROM expenses WHERE monthId = :monthId")
    fun getExpensesForMonth(monthId: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(CASE WHEN category = 'Income' THEN amount ELSE -amount END) FROM expenses WHERE monthId = :monthId")
    fun getTotalExpenseForMonth(monthId: Int): Flow<Int>
}