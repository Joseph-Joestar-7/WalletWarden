package com.example.walletwarden.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.walletwarden.database.ExpenseDatabase
import com.example.walletwarden.database.ExpenseEntity
import com.example.walletwarden.database.ExpenseRepository
import kotlinx.coroutines.launch
import java.util.Date

class MonthScreenViewModel(private val repository: ExpenseRepository, private val monthId: Int):ViewModel() {

    val expenses = repository.getExpensesForMonth(monthId).asLiveData()
    val totalExpense = repository.getTotalExpenseForMonth(monthId).asLiveData()

    fun addExpense(name: String, date: Date, amount: Int, category: String) {
        viewModelScope.launch {
            val newExpense = ExpenseEntity(monthId = monthId, name = name, date = date, amount = amount, category = category)
            repository.insert(newExpense)
        }
    }

    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch {
            repository.delete(expenseId)
        }
    }



    class MonthScreenViewModelFactory(private val context: Context, private val monthId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val expenseDao = ExpenseDatabase.getDatabase(context).expenseDao()
            val repository = ExpenseRepository(expenseDao)
            if (modelClass.isAssignableFrom(MonthScreenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MonthScreenViewModel(repository, monthId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}