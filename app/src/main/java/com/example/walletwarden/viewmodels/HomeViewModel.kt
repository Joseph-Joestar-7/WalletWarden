package com.example.walletwarden.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.walletwarden.database.MonthEntity
import com.example.walletwarden.database.MonthRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MonthRepository): ViewModel() {
    val allMonths: LiveData<List<MonthEntity>> = repository.allMonths.asLiveData()

    fun addNewMonth(month: String,year:Int) {
        viewModelScope.launch {
            val newMonth = MonthEntity(month = month,year=year, monthlyexp = 0)
            repository.insert(newMonth)
        }
    }

    fun deleteMonth(monthEntity: MonthEntity) {
        viewModelScope.launch {
            repository.delete(monthEntity)
        }
    }

    fun editMonth(month: String,year:Int,monthEntity: MonthEntity){
        viewModelScope.launch {
            repository.update(month,year,monthEntity)
        }
    }

    class HomeViewModelFactory(private val repository: MonthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}