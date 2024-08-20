package com.example.walletwarden.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.walletwarden.database.ExpenseDatabase
import com.example.walletwarden.database.ExpenseRepository

class MonthScreenViewModel(private val repository: ExpenseRepository, private val monthId: Int):ViewModel() {

    





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