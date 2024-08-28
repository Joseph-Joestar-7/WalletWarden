package com.example.walletwarden.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.walletwarden.database.ExpenseDatabase
import com.example.walletwarden.database.ExpenseEntity
import com.example.walletwarden.database.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MonthScreenViewModel(private val repository: ExpenseRepository, private val monthId: Int):ViewModel() {

    val expenses: LiveData<List<ExpenseEntity>> = repository.getExpensesForMonth(monthId)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    val totalExpense = repository.getTotalExpenseForMonth(monthId)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)


    suspend fun getMonthName():String{
        val monthName = repository.getMonthName(monthId)
        println("Fetched Month Name: $monthName for monthId: $monthId")
        return monthName ?: "Unknown Month"
    }

    suspend fun getYear():Int{
        val year = repository.getYear(monthId)
        println("Fetched Year: $year for monthId: $monthId")
        return year ?: 0
    }

    fun addExpense(
        name: String,date: Date,
         amount: Int,
        icon:Int, isExpense:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val newExpense = ExpenseEntity(
                monthId = monthId, name = name, date = date,
                amount = amount, icon =icon, isExpense = isExpense)
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