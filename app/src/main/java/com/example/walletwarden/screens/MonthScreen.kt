package com.example.walletwarden.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walletwarden.database.ExpenseRepository
import com.example.walletwarden.viewmodels.MonthScreenViewModel

@Composable
fun MonthExpenseScreen(monthId: Int) {
    val context = LocalContext.current
    val viewModel: MonthScreenViewModel = viewModel(
        factory = MonthScreenViewModel.MonthScreenViewModelFactory(context, monthId)
    )
    
}